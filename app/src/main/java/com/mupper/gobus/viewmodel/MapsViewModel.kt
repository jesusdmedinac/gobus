package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.gobus.scope.ScopedViewModel
import com.mupper.core.utils.LatLng
import com.mupper.gobus.repository.LocationRepository
import kotlinx.coroutines.launch
import java.util.*

class MapsViewModel(private val locationRepository: LocationRepository) : ScopedViewModel() {

    private lateinit var googleMap: GoogleMap
    private lateinit var userMarker: Marker
    private var timer: Timer

    private val _model = MutableLiveData<MapsModel>();
    val model: LiveData<MapsModel>
        get() {
            if (_model.value == null) refresh()
            return _model
        }

    sealed class MapsModel {
        class MapReady(val onMapReady: OnMapReadyCallback) : MapsModel()
        class RequestLocationPermissions : MapsModel()
        object RequestNewLocation : MapsModel()
        class NewLocation(val lastLocation: LatLng): MapsModel()
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
        timer.cancel()
    }

    fun onPermissionsRequested() {
        _model.value = MapsModel.MapReady(OnMapReadyCallback { loadedMap ->
            loadedMap.let { googleMap = it  }

            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    requestNewLocation()
                }
            }, 0, 5000)
        })
    }

    fun requestNewLocation() {
        launch {
            _model.value = MapsModel.RequestNewLocation
        }
    }

    fun onNewLocationRequested() {
        launch {
            val lastLocation = locationRepository.findLastLocation()?.getLatLng()
            lastLocation?.let {
                onLocationChanged(LatLng(it.latitude, it.longitude))
            }

            googleMap.addMarker(lastLocation?.let {
                MarkerOptions().position(it).title("User position")
            }).let {
                userMarker = it
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 17f))
        }
    }

    fun onLocationChanged(lastLocation: LatLng) {
        _model.value = MapsModel.NewLocation(lastLocation)
    }
}