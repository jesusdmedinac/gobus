package com.mupper.data.source.local

import com.mupper.domain.traveler.Traveler

interface TravelerLocalDataSource {
    suspend fun getTravelerCount(): Int

    suspend fun insertTraveler(travelingPath: String, traveler: Traveler)

    suspend fun findTravelerByEmail(email: String): Traveler?

    suspend fun shareActualLocation(traveler: Traveler)
}