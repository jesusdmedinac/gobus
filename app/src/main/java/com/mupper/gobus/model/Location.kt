package com.mupper.gobus.model

import android.location.Location as AndroidLocation
import com.google.android.gms.maps.model.LatLng

/**
 * Created by jesus.medina on 11/2019.
 * Insulet Corporation
 * Andromeda
 */
class Location(l: AndroidLocation?) : AndroidLocation(l) {
    fun getLatLng() = LatLng(latitude, longitude)
}