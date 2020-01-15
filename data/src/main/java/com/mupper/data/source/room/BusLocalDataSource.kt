package com.mupper.data.source.room

import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers

interface BusLocalDataSource {
    fun getCount(path: String): Int

    fun getTravelingBusWithTravelers() : List<BusWithTravelers>

    suspend fun addNewBus(bus: Bus)

    suspend fun shareActualLocation(bus: Bus)
}