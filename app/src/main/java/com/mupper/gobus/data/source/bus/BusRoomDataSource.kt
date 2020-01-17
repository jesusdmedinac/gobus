package com.mupper.gobus.data.source.bus

import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.gobus.data.toRoomBus
import com.mupper.gobus.data.toDomainBusWithTravelers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusRoomDataSource(db: GobusDatabase) : BusLocalDataSource {

    private val busDao = db.busDao()

    override suspend fun getCount(path: String): Int =
        withContext(Dispatchers.IO) {
            busDao.getCount(path)
        }

    override suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        withContext(Dispatchers.IO) {
            val busWithTravelers = busDao.getBusWithTravelers()
            busWithTravelers.map {
                it.toDomainBusWithTravelers()
            }
        }

    override suspend fun addNewBus(bus: DomainBus) = withContext(Dispatchers.IO) {
        busDao.insertBus(bus.toRoomBus())
    }

    override suspend fun shareActualLocation(bus: BusWithTravelers) = withContext(Dispatchers.IO) {
        busDao.updateBus(bus.toRoomBus())
    }
}
