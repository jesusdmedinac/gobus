package com.mupper.gobus.data.database.traveler

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mupper.gobus.data.database.CurrentPosition

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
@Entity(tableName = "traveler")
data class Traveler(
    @PrimaryKey @ColumnInfo(name = "email") val email: String,
    @Embedded val currentPosition: CurrentPosition,
    @ColumnInfo(name = "traveling_path") val travelingPath: String,
    @ColumnInfo(name = "is_traveling") val isTraveling: Boolean
)