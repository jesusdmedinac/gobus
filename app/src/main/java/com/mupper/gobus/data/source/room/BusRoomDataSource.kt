package com.mupper.gobus.data.source.room

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.mapper.toDomainBusWithTravelers
import com.mupper.gobus.data.mapper.toRoomBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mupper.domain.bus.Bus as DomainBus

class BusRoomDataSource(db: GobusDatabase) : BusLocalDataSource {

    private val busDao = db.busDao()

    override suspend fun getBusCount(path: String): Int =
        withContext(Dispatchers.IO) {
            busDao.busCount(path)
        }

    override suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        withContext(Dispatchers.IO) {
            busDao.getBusWithTravelers().map { it.toDomainBusWithTravelers() }
        }

    override suspend fun addNewBus(bus: DomainBus) {
        GlobalScope.launch(Dispatchers.IO) {
            busDao.insertBus(bus.toRoomBus())
        }
    }

    override suspend fun shareActualLocation(bus: BusWithTravelers) {
        GlobalScope.launch(Dispatchers.IO) {
            busDao.updateBus(bus.toRoomBus())
        }
    }
}
