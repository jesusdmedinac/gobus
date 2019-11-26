package com.mupper.gobus.model

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 * Created by jesus.medina on 11/2019.
 * Insulet Corporation
 * Andromeda
 */
class UserLocation(l: Location?) : Location(l) {
    fun getLatLng() = LatLng(latitude, longitude)
}