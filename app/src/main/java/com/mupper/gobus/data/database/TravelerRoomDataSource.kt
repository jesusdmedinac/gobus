package com.mupper.gobus.data.database

import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.data.toDomainTraveler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mupper.domain.traveler.Traveler as DomainTraveler

class TravelerRoomDataSource(db: GobusDatabase) : TravelerLocalDataSource {
    private val actualEmail = "dmc12345628@gmail.com"

    private val travelerDao = db.travelerDao()

    override fun getActualTraveler(): DomainTraveler =
        travelerDao.getActualTraveler().toDomainTraveler()

    override suspend fun shareActualLocation(newLocation: LatLng) = withContext(Dispatchers.IO) {
        val (latitude, longitude) = newLocation
        travelerDao.updateTraveler(actualEmail, latitude, longitude)
    }
}
