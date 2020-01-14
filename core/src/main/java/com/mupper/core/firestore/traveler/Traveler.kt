package com.mupper.core.firestore.traveler

import com.mupper.core.firestore.CurrentPosition


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
data class Traveler(
    val currentPosition: CurrentPosition,
    val email: String,
    val isTraveling: Boolean
)