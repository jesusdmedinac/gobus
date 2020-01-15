package com.mupper.features.bus

import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.domain.bus.Bus

class GetTravelingBus(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun getActualBus(): Bus {
        val busWithTravelers = busLocalDataSource.getTravelingBusWithTravelers()

        return busRemoteDataSource.findBusByPathName(busWithTravelers[0].path)
    }
}