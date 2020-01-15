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

    override fun getCount(path: String) = busDao.getCount(path)

    override fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        busDao.getBusWithTravelers().map {
            it.toDomainBusWithTravelers()
        }

    override suspend fun addNewBus(bus: DomainBus) = withContext(Dispatchers.IO) {
        busDao.insertBus(bus.toRoomBus())
    }

    override suspend fun shareActualLocation(bus: DomainBus) = withContext(Dispatchers.IO) {
        busDao.updateBus(bus.toRoomBus())
    }
}
