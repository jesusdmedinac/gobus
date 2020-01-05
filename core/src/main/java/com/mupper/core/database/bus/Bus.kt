package com.mupper.core.database.bus

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
@Entity(tableName = "bus")
class Bus(
    @PrimaryKey val path: String,
    val color: String,
    val capacity: Int
)