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
import com.mupper.data.repository.MapResourcesRepository
import com.mupper.data.source.location.LocationDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.MILISECONDS_ONE_SECOND
import com.mupper.gobus.commons.MILLISECONDS_TEN_MILLIS
import com.mupper.gobus.commons.MILLISECONDS_SIXTEEN_MILLIS
import com.mupper.gobus.commons.GOOGLE_MAP_DEFAULT_ZOOM
import com.mupper.gobus.commons.scope.ScopedViewModel
import com.mupper.gobus.data.mapper.toDomainLatLng
import com.mupper.gobus.data.mapper.toMapsLatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.LatLng as MapsLatLng

// TODO: Extract traveler properties and function to TravelerViewModel
class MapsViewModel(
    private val locationDataSource: LocationDataSource<LocationRequest, LocationCallback>,
    private val mapResourcesRepository: MapResourcesRepository<BitmapDescriptor>,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

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
            val lastLatLng: LatLng? = locationDataSource.findLastLocation()
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
                interval = MILISECONDS_ONE_SECOND
            }
            locationDataSource.requestLocationUpdates(locationRequest, locationCallback)
        }
    }

    private fun moveMarkerToLastLocation(latLng: LatLng): LatLng {
        val mapsLatLng = latLng.toMapsLatLng()
        if (travelerMarkerOptions == null || travelerMarker == null) {
            travelerMarkerOptions = MarkerOptions().apply {
                position(mapsLatLng)?.title("User position")
                icon(mapResourcesRepository.getBusIcon())
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

        val duration = MILLISECONDS_TEN_MILLIS
        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed / duration).toFloat())

                val lat = t * finalLatLng.latitude + (1 - t) * startLatLng.latitude
                val lng = t * finalLatLng.longitude + (1 - t) * startLatLng.longitude

                travelerMarker?.position = LatLng(lat, lng).toMapsLatLng()

                val timeElapsed = 1.0
                if (t < timeElapsed) {
                    handler.postDelayed(this, MILLISECONDS_SIXTEEN_MILLIS)
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
                ), GOOGLE_MAP_DEFAULT_ZOOM
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
