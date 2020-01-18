package com.mupper.gobus.viewmodel

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.domain.LatLng
import com.mupper.gobus.commons.Event
import com.mupper.gobus.data.toDomainLatLng
import com.mupper.gobus.data.toMapsLatLng
import com.mupper.gobus.repository.LocationRepository
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.LatLng as MapsLatLng


class MapsViewModel(
    private val locationRepository: LocationRepository,
    private val bitmapDescriptor: BitmapDescriptor
) : ScopedViewModel() {

    private var googleMap: GoogleMap? = null
    private var travelerMarker: Marker? = null
    private var travelerMarkerOptions: MarkerOptions? = null
    private var isTraveling = false
    private lateinit var locationCallback: LocationCallback

    private val _model = MutableLiveData<Event<MapsModel>>()
    val model: LiveData<Event<MapsModel>> get() = _model

    private val _requestLocationPermission = MutableLiveData<Event<Unit>>()
    val requestLocationPermission: LiveData<Event<Unit>> get() = _requestLocationPermission

    sealed class MapsModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapsModel()
        object RequestNewLocation : MapsModel()
        class NewLocation(val lastLocation: LatLng, val isTraveling: Boolean) : MapsModel()
    }

    init {
        initScope()
    }

    fun requestLocationPermission() {
        _requestLocationPermission.value =
            Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        destroyScope()
    }

    fun onPermissionsRequested() {
        launch {
            _model.value = Event(
                MapsModel.MapReady(OnMapReadyCallback { loadedMap ->
                    initGoogleMap(loadedMap)
                })
            )
        }
    }

    private fun initGoogleMap(it: GoogleMap) {
        googleMap = it.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            isMyLocationEnabled = true
            isTrafficEnabled = false
            isIndoorEnabled = false
            isBuildingsEnabled = true
        }
        requestNewLocation()
    }

    private fun requestNewLocation() {
        _model.value = Event(MapsModel.RequestNewLocation)
    }

    fun startTravel() {
        isTraveling = true
    }

    fun stopTravel() {
        isTraveling = false
    }

    fun onNewLocationRequested() {
        launch {
            val lastLatLng: LatLng? = locationRepository.findLastLocation()?.getLatLng()
            lastLatLng?.let {
                onLocationChanged(it)

                animateCameraToLastLocation(it)
                moveMarkerToLastLocation(it)
            }
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        val latLng = location.toDomainLatLng()
                        onLocationChanged(latLng)

                        animateCameraToLastLocation(latLng)
                        moveMarkerToLastLocation(latLng)
                    }
                }
            }
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000
            }
            locationRepository.requestLocationUpdates(locationRequest, locationCallback)
        }
    }

    private fun moveMarkerToLastLocation(latLng: LatLng): LatLng {
        val mapsLatLng = latLng.toMapsLatLng()
        if (travelerMarkerOptions == null || travelerMarker == null) {
            travelerMarkerOptions = MarkerOptions().apply {
                position(mapsLatLng)
                    ?.title("User position")
                icon(bitmapDescriptor)
            }

            googleMap?.addMarker(travelerMarkerOptions)?.let {
                travelerMarker = it
            }
        } else {
            travelerMarkerOptions?.position(mapsLatLng)?.let {
                travelerMarkerOptions = it
            }
            travelerMarker?.position = mapsLatLng
        }

        shouldDrawMarker {
            smoothMoveMarker(latLng)
        }

        return latLng
    }

    private fun smoothMoveMarker(finalLatLng: LatLng) {
        val startLatLng = travelerMarker?.position?.toDomainLatLng() ?: LatLng(0.0, 0.0)

        val handler = Handler()
        val start = SystemClock.uptimeMillis()

        val duration = 10
        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed / duration).toFloat())

                val lat = t * finalLatLng.latitude + (1 - t) * startLatLng.latitude
                val lng = t * finalLatLng.longitude + (1 - t) * startLatLng.longitude

                travelerMarker?.position = LatLng(lat, lng).toMapsLatLng()

                if (t < 1.0) {
                    handler.postDelayed(this, 16)
                }
            }
        })
    }

    private fun shouldDrawMarker(markerAction: () -> Unit) {
        travelerMarker?.isVisible = isTraveling
        googleMap?.isMyLocationEnabled = !isTraveling
        if (isTraveling) {
            markerAction()
        }
    }

    private fun animateCameraToLastLocation(it: LatLng) {
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                MapsLatLng(
                    it.latitude,
                    it.longitude
                ), 17f
            )
        )
    }

    private fun onLocationChanged(lastLocation: LatLng) {
        _model.value = Event(
            MapsModel.NewLocation(
                lastLocation,
                isTraveling
            )
        )
    }

    fun clearMap() {
        googleMap = null
        travelerMarker = null
        travelerMarkerOptions = null
    }
}