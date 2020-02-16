package com.mupper.gobus.viewmodel

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.data.repository.MapResourcesRepository
import com.mupper.data.source.location.LocationDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.commons.*
import com.mupper.gobus.commons.scope.ScopedViewModel
import com.mupper.gobus.data.mapper.toDomainLatLng
import com.mupper.gobus.data.mapper.toMapsLatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.LatLng as MapsLatLng

// TOD O: Extract traveler properties and function to TravelerViewModel
class MapViewModel(
    private val locationDataSource: LocationDataSource<LocationRequest, LocationCallback>,
    private val mapResourcesRepository: MapResourcesRepository<BitmapDescriptor>,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    private var googleMap: GoogleMap? = null
    private var travelerMarker: Marker? = null
    private var travelerMarkerOptions: MarkerOptions? = null
    private var isTraveling = false
    private lateinit var locationCallback: LocationCallback

    private val _mapEventLiveData = MutableLiveData<Event<MapsModel>>()
    val mapsEventLiveData: LiveData<Event<MapsModel>> get() = _mapEventLiveData

    private val _requestLocationPermissionEventLiveData = MutableLiveData<Event<Unit>>()
    val requestLocationPermissionEventLiveData: LiveData<Event<Unit>> get() = _requestLocationPermissionEventLiveData

    sealed class MapsModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapsModel()
        object RequestNewLocation : MapsModel()
        class NewLocation(val lastLocation: LatLng, val isTraveling: Boolean) : MapsModel()
    }

    init {
        initScope()
    }

    fun requestLocationPermission() {
        _requestLocationPermissionEventLiveData.value =
            Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        destroyScope()
    }

    fun onPermissionsRequested() {
        launch {
            _mapEventLiveData.value = Event(
                MapsModel.MapReady(OnMapReadyCallback { loadedMap ->
                    initGoogleMap(loadedMap)
                })
            )
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun initGoogleMap(it: GoogleMap) {
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
        _mapEventLiveData.value = Event(MapsModel.RequestNewLocation)
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
            // TOD O: Move to another place where they can be initialized once
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

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun smoothMoveMarker(finalLatLng: LatLng) {
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

    private fun animateCameraToLastLocation(newLatLng: LatLng) {
        val newLatLngZoom = generateNewLatLngZoom(newLatLng)
        googleMap?.animateCamera(
            newLatLngZoom
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun generateNewLatLngZoom(newLatLng: LatLng): CameraUpdate? {
        val (latitude, longitude) = newLatLng
        return CameraUpdateFactory.newLatLngZoom(
            MapsLatLng(
                latitude,
                longitude
            ), GOOGLE_MAP_DEFAULT_ZOOM
        )
    }

    private fun onLocationChanged(lastLocation: LatLng) {
        _mapEventLiveData.value = Event(
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
