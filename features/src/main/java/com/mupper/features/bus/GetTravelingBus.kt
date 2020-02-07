package com.mupper.features.bus

import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.domain.relations.BusWithTravelers

class GetTravelingBus(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun getActualBusWithTravelers(): BusWithTravelers? {
        val busWithTravelers: List<BusWithTravelers> = busLocalDataSource.getTravelingBusWithTravelers()
        if (busWithTravelers.isEmpty()) {
            return null
        }

        return busWithTravelers[0]
    }
}