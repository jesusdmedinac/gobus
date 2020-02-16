package com.mupper.gobus.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mupper.gobus.data.database.bus.Bus
import com.mupper.gobus.data.database.traveler.Traveler

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
data class BusWithTravelers(
    @Embedded val bus: Bus,
    @Relation(
        parentColumn = "path",
        entityColumn = "traveling_path"
    )
    val travelers: List<Traveler>
)
