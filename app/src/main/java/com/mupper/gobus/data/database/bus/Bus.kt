package com.mupper.gobus.data.database.bus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
@Entity(tableName = "bus")
data class Bus(
    @PrimaryKey val path: String,
    val color: String,
    val capacity: Int,
    @ColumnInfo(name = "is_traveling") val isTraveling: Boolean
)