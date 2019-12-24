package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng as MapsLatLng
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.core.utils.LatLng
import com.mupper.gobus.repository.LocationRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.logging.Handler

class MapsViewModel(private val locationRepository: LocationRepository) : ScopedViewModel() {

    private lateinit var googleMap: GoogleMap
    private var travelerMarker: Marker? = null
    private var travelerMarkerOptions: MarkerOptions? = null
    private var timer: Timer?
    private var isTraveling = false

    private val _model = MutableLiveData<MapsModel>()
    val model: LiveData<MapsModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class MapsModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapsModel()
        class RequestLocationPermissions : MapsModel()
        object RequestNewLocation : MapsModel()
        class NewLocation(val lastLocation: LatLng, val isTrvaling: Boolean) : MapsModel()
    }

    init {
        initScope()
        timer = Timer()
    }

    private fun refresh() {
        _model.value = MapsModel.RequestLocationPermissions()
    }

    override fun onCleared() {
        super.onCleared()
        destroyScope()
        timer?.cancel()
        timer = Timer()
    }

    fun onPermissionsRequested() {
        _model.value = MapsModel.MapReady(OnMapReadyCallback { loadedMap ->
            loadedMap.let {
                initGoogleMap(it);
            }

            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    requestNewLocation()
                }
            }, 0, 5000)
        })
    }

    private fun initGoogleMap(it: GoogleMap) {
        googleMap = it
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)
        googleMap.setMyLocationEnabled(true)
        googleMap.setTrafficEnabled(false)
        googleMap.setIndoorEnabled(false)
        googleMap.setBuildingsEnabled(true)
    }

    fun requestNewLocation() {
        launch {
            _model.value = MapsModel.RequestNewLocation
        }
    }

    fun startTravel() {
        isTraveling = true
    }

    fun stopTravel() {
        isTraveling = false
    }

    fun onNewLocationRequested() {
        launch {
            val lastLocation: LatLng? = locationRepository.findLastLocation()?.getLatLng()
            lastLocation?.let {
                onLocationChanged(LatLng(it.latitude, it.longitude))

                animateCameraToLastLocation(it)
                moveMarkerToLastLocation(it)
            }
        }
    }

    private fun moveMarkerToLastLocation(it: LatLng): LatLng {
        val priorLocaiton: MapsLatLng?
        val lastLocation = MapsLatLng(it.latitude, it.longitude)
        if (travelerMarkerOptions == null) {
            travelerMarkerOptions = MarkerOptions()
            travelerMarkerOptions?.position(lastLocation)
                ?.title("User position")
            googleMap.addMarker(travelerMarkerOptions).let {
                travelerMarker = it
            }
        } else {
            travelerMarkerOptions?.position(lastLocation).let {
                travelerMarkerOptions = it
            }
            priorLocaiton = travelerMarker?.position
            travelerMarker?.position = lastLocation
        }

        smoothMoveMarker()

        return it
    }

    private fun smoothMoveMarker() {
        val handler = Handler()
    }

    private fun animateCameraToLastLocation(it: LatLng) {
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                MapsLatLng(
                    it.latitude,
                    it.longitude
                ), 17f
            )
        )
    }

    fun onLocationChanged(lastLocation: LatLng) {
        _model.value = MapsModel.NewLocation(lastLocation, isTraveling)
    }
}