package com.mupper.data.source.firestore

import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.domain.traveler.Traveler

interface BusRemoteDataSource {
    fun addNewBus(bus: Bus)

    suspend fun findBusByPathName(path: String) : Bus

    suspend fun findBusTravelersByPathName(path: String) : List<Traveler>

    suspend fun shareActualLocation(bus: Bus, traveler: Traveler)
}