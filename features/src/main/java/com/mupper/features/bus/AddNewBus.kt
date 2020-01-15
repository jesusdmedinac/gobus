package com.mupper.features.bus

import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.domain.bus.Bus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNewBus(
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun invoke(bus: Bus) = withContext(Dispatchers.IO) {
        busLocalDataSource.addNewBus(bus)

        if (busLocalDataSource.getCount(bus.path) == 0) {
            busRemoteDataSource.addNewBus(bus)
        }
    }
}