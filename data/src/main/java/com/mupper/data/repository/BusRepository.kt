package com.mupper.data.repository

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.domain.traveler.Traveler

interface BusRepository {
    suspend fun addNewBusWithTravelers(bus: Bus, traveler: Traveler)

    suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers>
}

private class BusRepositoryImpl(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) : BusRepository {
    override suspend fun addNewBusWithTravelers(bus: Bus, traveler: Traveler) {
        busLocalDataSource.addNewBus(bus)
        busRemoteDataSource.addNewBusWithTravelers(bus, traveler)
    }

    override suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        busLocalDataSource.getTravelingBusWithTravelers()
}

class BusRepositoryDerived(
    busLocalDataSource: BusLocalDataSource,
    busRemoteDataSource: BusRemoteDataSource
) : BusRepository by BusRepositoryImpl(busLocalDataSource, busRemoteDataSource)