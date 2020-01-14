package com.mupper.core.firestore.bus

import com.mupper.core.database.traveler.Traveler


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
data class Bus(
    val path: String,
    val color: String,
    val capacity: Int,
    val travelers: List<Traveler>
)