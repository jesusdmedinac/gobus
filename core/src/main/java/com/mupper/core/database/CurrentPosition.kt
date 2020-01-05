package com.mupper.core.database

import androidx.room.Entity


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
@Entity(tableName = "LatLng")
class CurrentPosition(
    val latitude: Double,
    val longitude: Double
)