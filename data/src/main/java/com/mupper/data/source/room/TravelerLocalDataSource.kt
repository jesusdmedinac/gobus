package com.mupper.data.source.room

import com.mupper.domain.traveler.Traveler

interface TravelerLocalDataSource {
    suspend fun getCount(): Int

    suspend fun insertTraveler(travelingPath: String, traveler: Traveler)

    suspend fun findTravelerByEmail(email: String): Traveler?

    suspend fun shareActualLocation(traveler: Traveler)
}