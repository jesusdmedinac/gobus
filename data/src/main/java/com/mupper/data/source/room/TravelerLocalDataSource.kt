package com.mupper.data.source.room

import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler

interface TravelerLocalDataSource {

    fun getActualTraveler(): Traveler

    suspend fun shareActualLocation(newLocation: LatLng)
}