package com.mupper.features.bus

import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.domain.bus.Bus
import com.mupper.domain.traveler.Traveler
import com.mupper.features.traveler.GetActualTraveler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNewBusWithTravelers(
    private val getActualTraveler: GetActualTraveler,
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun invoke(bus: Bus) = withContext(Dispatchers.IO) {
        val traveler = getActualTraveler.invoke(bus.path)
        traveler?.let {
            busRemoteDataSource.addNewBusWithTravelers(bus, traveler)
        }
        busLocalDataSource.addNewBus(bus)
    }
}