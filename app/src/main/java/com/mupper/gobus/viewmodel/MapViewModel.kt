package com.mupper.gobus.viewmodel

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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.data.source.location.LocationDataSource
import com.mupper.data.source.resources.MapMarkerDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.GOOGLE_MAP_DEFAULT_ZOOM
import com.mupper.gobus.commons.MILISECONDS_ONE_SECOND
import com.mupper.gobus.commons.scope.ScopedViewModel
import com.mupper.gobus.data.mapper.toDomainLatLng
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import com.google.android.gms.maps.model.LatLng as MapsLatLng

class MapViewModel(
    private val locationDataSource: LocationDataSource<LocationRequest, LocationCallback>,
    private val travelerMapMarkerDataSource: MapMarkerDataSource<Marker, MarkerOptions>,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var googleMap: GoogleMap? = null

    private val _mapEventLiveData = MutableLiveData<Event<MapModel>>()
    val mapEventLiveData: LiveData<Event<MapModel>> get() = _mapEventLiveData

    private val _requestLocationPermissionEventLiveData = MutableLiveData<Event<Unit>>()
    val requestLocationPermissionEventLiveData: LiveData<Event<Unit>> get() = _requestLocationPermissionEventLiveData

    sealed class MapModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapModel()
        object RequestNewLocation : MapModel()
        class NewLocation(val lastLocation: LatLng, val isTraveling: Boolean) : MapModel()
    }

    fun requestLocationPermission() {
        _requestLocationPermissionEventLiveData.value =
            Event(Unit)
    }

    fun onPermissionsRequested() {
        launch {
            _mapEventLiveData.value = Event(
                MapModel.MapReady(OnMapReadyCallback { loadedMap ->
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
        prepareLocationUpdates()
    }

    private fun requestNewLocation() {
        _mapEventLiveData.value = Event(MapModel.RequestNewLocation)
    }

    private fun prepareLocationUpdates() {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                this@MapViewModel.onLocationResult(locationResult)
            }
        }
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = MILISECONDS_ONE_SECOND
        }
        locationDataSource.requestLocationUpdates(locationRequest, locationCallback)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onLocationResult(locationResult: LocationResult?) {
        locationResult ?: return
        for (location in locationResult.locations) {
            val latLng = location.toDomainLatLng()
            onLocationUpdate(latLng)
        }
    }

    private fun onLocationUpdate(latLng: LatLng) {
        onLocationChanged(latLng)
        animateCameraToLastLocation(latLng)
        moveMarkerToLastLocation(latLng)
    }

    fun startTravel() {
        travelerMapMarkerDataSource.visibleForMap = true
    }

    fun stopTravel() {
        travelerMapMarkerDataSource.visibleForMap = false
    }

    fun onNewLocationRequested() {
        launch {
            findLastLocation()
        }
    }

    private suspend fun findLastLocation() {
        val lastLatLng: LatLng? = locationDataSource.findLastLocation()
        lastLatLng?.let {
            onLocationUpdate(it)
        }
    }

    private fun moveMarkerToLastLocation(latLng: LatLng): LatLng =
        travelerMapMarkerDataSource.moveMarkerToLastLocation({ enableUserLocation ->
            googleMap?.isMyLocationEnabled = enableUserLocation
        }, latLng) {
            googleMap?.addMarker(it)
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
            MapModel.NewLocation(
                lastLocation,
                travelerMapMarkerDataSource.visibleForMap
            )
        )
    }

    fun clearMap() {
        googleMap = null
        travelerMapMarkerDataSource.clearMarker()
    }
}
