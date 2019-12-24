package com.mupper.gobus.model

import com.mupper.core.utils.LatLng
import android.location.Location as AndroidLocation

/**
 * Created by jesus.medina on 11/2019.
 * Insulet Corporation
 * Andromeda
 */
class Location(l: AndroidLocation?) : AndroidLocation(l) {
    fun getLatLng() = LatLng(latitude, longitude)
}