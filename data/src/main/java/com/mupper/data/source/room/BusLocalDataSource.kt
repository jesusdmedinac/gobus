package com.mupper.data.source.room

import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers

interface BusLocalDataSource {
    suspend fun getCount(path: String): Int

    suspend fun getTravelingBusWithTravelers() : List<BusWithTravelers>

    suspend fun addNewBus(bus: Bus)

    suspend fun shareActualLocation(bus: BusWithTravelers)
}