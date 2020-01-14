package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.core.utils.LatLng
import com.mupper.gobus.commons.Event
import com.mupper.gobus.repository.LocationRepository
import kotlinx.coroutines.launch
import java.util.*
import com.google.android.gms.maps.model.LatLng as MapsLatLng

class MapsViewModel(private val locationRepository: LocationRepository) : ScopedViewModel() {

    private var googleMap: GoogleMap? = null
    private var travelerMarker: Marker? = null
    private var travelerMarkerOptions: MarkerOptions? = null
    private var timer: Timer?
    private var isTraveling = false

    private val _model = MutableLiveData<Event<MapsModel>>()
    val model: LiveData<Event<MapsModel>> get() = _model

    private val _requestCoarsePermission = MutableLiveData<Event<Unit>>()
    val requestCoarsePermission: LiveData<Event<Unit>> get() = _requestCoarsePermission

    sealed class MapsModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapsModel()
        object RequestNewLocation : MapsModel()
        class NewLocation(val lastLocation: LatLng, val isTrvaling: Boolean) : MapsModel()
    }

    init {
        initScope()
        timer = Timer()
    }

    fun requestCoarsePermission() {
        _requestCoarsePermission.value =
            Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        destroyScope()
        timer?.cancel()
        timer = Timer()
    }

    fun onPermissionsRequested() {
        launch {
            _model.value = Event(
                MapsModel.MapReady(OnMapReadyCallback { loadedMap ->
                    loadedMap.let {
                        initGoogleMap(it)
                    }
                })
            )

            timer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    requestNewLocation()
                }
            }, 0, 5000)
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
    }

    fun requestNewLocation() {
        launch {
            _model.value = Event(MapsModel.RequestNewLocation)
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
//        val priorLocation: MapsLatLng?
        val lastLocation = MapsLatLng(it.latitude, it.longitude)
        if (travelerMarkerOptions == null) {
            travelerMarkerOptions = MarkerOptions()
            travelerMarkerOptions?.position(lastLocation)
                ?.title("User position")
            googleMap?.addMarker(travelerMarkerOptions).let {
                travelerMarker = it
            }
        } else {
            travelerMarkerOptions?.position(lastLocation).let {
                travelerMarkerOptions = it
            }
//            priorLocation = travelerMarker?.position
            travelerMarker?.position = lastLocation
        }

        smoothMoveMarker()

        return it
    }

    private fun smoothMoveMarker() {
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

    fun onLocationChanged(lastLocation: LatLng) {
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