package com.mupper.gobus.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.gobus.repository.UserLocationRepository
import com.mupper.gobus.ui.common.Scope
import kotlinx.coroutines.launch

class MapsViewModel(activity: Activity) : ViewModel(),
    Scope by Scope.Impl() {

    private var userLastLocation: LatLng? = null
    private var userLocationRepository: UserLocationRepository = UserLocationRepository(activity)

    private lateinit var gobusMap: GoogleMap

    init {
        initScope()
    }

    fun getMapReady() = OnMapReadyCallback {googleMap ->
        gobusMap = googleMap

        launch {
            userLastLocation = userLocationRepository.findLastLocation()?.getLatLng()

            gobusMap.addMarker(userLastLocation?.let {
                MarkerOptions().position(it).title("User position")
            })
            gobusMap.moveCamera(CameraUpdateFactory.newLatLng(userLastLocation))
        }
    }

    override fun onCleared() {
        super.onCleared()
        destroyScope()
    }
}