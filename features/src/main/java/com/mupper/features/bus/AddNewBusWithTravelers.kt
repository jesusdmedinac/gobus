package com.mupper.features.bus

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.domain.bus.Bus
import com.mupper.features.traveler.GetActualTraveler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddNewBusWithTravelers(
    private val getActualTraveler: GetActualTraveler,
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun invoke(bus: Bus) = withContext(dispatcher) {
        val traveler = getActualTraveler.invoke(bus.path)
        traveler?.let {
            busRemoteDataSource.addNewBusWithTravelers(bus, traveler)
        }
        busLocalDataSource.addNewBus(bus)
    }
}