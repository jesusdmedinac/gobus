package com.mupper.domain.bus

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
data class Bus(
    val path: String,
    val color: String,
    val capacity: Int,
    val isTraveling: Boolean
)