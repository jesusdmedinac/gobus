package com.mupper.gobus.model

import com.mupper.gobus.data.toDomainLatLng
import android.location.Location as AndroidLocation

/**
 * Created by jesus.medina on 11/2019.
 * Mupper
 */
class Location(l: AndroidLocation?) : AndroidLocation(l) {
    fun getLatLng() = toDomainLatLng()
}