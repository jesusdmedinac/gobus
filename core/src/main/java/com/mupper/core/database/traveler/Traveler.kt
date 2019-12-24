package com.mupper.core.database.traveler

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
@Entity(tableName = "traveler")
class Traveler(
    @PrimaryKey @ColumnInfo(name = "email") val email: String,
    @Embedded val currentPosition: CurrentPosition,
    @ColumnInfo(name = "is_traveling") val isTraveling: Boolean
)