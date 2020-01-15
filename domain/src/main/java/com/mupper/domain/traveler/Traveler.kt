package com.mupper.domain.traveler

import com.mupper.domain.LatLng


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
data class Traveler(
    val email: String,
    val currentPosition: LatLng,
    val isTraveling: Boolean
)