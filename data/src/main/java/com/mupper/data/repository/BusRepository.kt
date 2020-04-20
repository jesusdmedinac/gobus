package com.mupper.data.repository

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.domain.traveler.Traveler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface BusRepository {
    suspend fun addNewBusWithTravelers(bus: Bus, traveler: Traveler)

    suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers>

    suspend fun shareActualLocation(travelingBus: BusWithTravelers, traveler: Traveler)
}

private class BusRepositoryImpl(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : BusRepository {
    override suspend fun addNewBusWithTravelers(bus: Bus, traveler: Traveler) {
        withContext(dispatcher) { busLocalDataSource.addNewBus(bus) }
        withContext(dispatcher) { busRemoteDataSource.addNewBusWithTravelers(bus, traveler) }
    }

    override suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        withContext(dispatcher) { busLocalDataSource.getTravelingBusWithTravelers() }

    override suspend fun shareActualLocation(travelingBus: BusWithTravelers, traveler: Traveler) {
        withContext(dispatcher) { busLocalDataSource.shareActualLocation(travelingBus) }
        withContext(dispatcher) { busRemoteDataSource.shareActualLocation(travelingBus, traveler) }
    }
}

class BusRepositoryDerived(
    busLocalDataSource: BusLocalDataSource,
    busRemoteDataSource: BusRemoteDataSource,
    dispatcher: CoroutineDispatcher
) : BusRepository by BusRepositoryImpl(busLocalDataSource, busRemoteDataSource, dispatcher)