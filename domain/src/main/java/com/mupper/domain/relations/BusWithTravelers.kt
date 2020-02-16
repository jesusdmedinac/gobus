package com.mupper.domain.relations

import com.mupper.domain.traveler.Traveler

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
data class BusWithTravelers(
    val path: String,
    val color: String,
    val capacity: Int,
    val travelers: List<Traveler>
)
