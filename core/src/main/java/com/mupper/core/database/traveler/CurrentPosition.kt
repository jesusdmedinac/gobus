package com.mupper.core.database.traveler

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
@Entity(tableName = "LatLng")
class CurrentPosition(
    val latitude: Double,
    val longitude: Double
)