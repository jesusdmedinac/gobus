package com.mupper.gobus.data.source.room

import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.mapper.toDomainTraveler
import com.mupper.gobus.data.mapper.toRoomTraveler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.mupper.domain.traveler.Traveler as DomainTraveler

class TravelerRoomDataSource(db: GobusDatabase) : TravelerLocalDataSource {
    private val travelerDao = db.travelerDao()

    override suspend fun getCount(): Int =
        withContext(Dispatchers.IO) {
            travelerDao.travelerCount()
        }

    override suspend fun insertTraveler(travelingPath: String, traveler: DomainTraveler) {
        GlobalScope.launch(Dispatchers.IO) {
            travelerDao.insertTraveler(traveler.toRoomTraveler(travelingPath))
        }
    }

    override suspend fun findTravelerByEmail(email: String): DomainTraveler? =
        withContext(Dispatchers.IO) {
            travelerDao.findTravelerByEmail(email)?.toDomainTraveler()
        }

    override suspend fun shareActualLocation(traveler: DomainTraveler) {
        GlobalScope.launch(Dispatchers.IO) {
            val (email, currentPosition) = traveler
            val (latitude, longitude) = currentPosition
            travelerDao.updateTraveler(email, latitude, longitude)
        }
    }
}
