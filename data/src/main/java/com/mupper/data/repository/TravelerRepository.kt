package com.mupper.data.repository

import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler

interface TravelerRepository {
    suspend fun retrieveActualTraveler(travelingPath: String): Traveler?
    suspend fun shareActualLocation(travelerInBus: Traveler)
}

private class TravelerRepositoryImpl(
    private val actualEmail: String,
    private val travelerLocalDataSource: TravelerLocalDataSource,
    private val travelerRemoteDataSource: TravelerRemoteDataSource
) : TravelerRepository {
    override suspend fun retrieveActualTraveler(travelingPath: String): Traveler? {
        if (travelerLocalDataSource.getTravelerCount() == 0) {
            val remoteTraveler = travelerRemoteDataSource.findTravelerByEmail(actualEmail)
                ?: addStaticTraveler()

            travelerLocalDataSource.insertTraveler(travelingPath, remoteTraveler)
        }
        return travelerLocalDataSource.findTravelerByEmail(actualEmail)
    }

    override suspend fun shareActualLocation(travelerInBus: Traveler) {
        travelerLocalDataSource.shareActualLocation(travelerInBus)
        travelerRemoteDataSource.shareActualLocation(travelerInBus)
    }

    private suspend fun addStaticTraveler(): Traveler = travelerRemoteDataSource.addTraveler(
        Traveler(
            actualEmail,
            LatLng(
                0.0,
                0.0
            ),
            false
        )
    )
}

class TravelerRepositoryDerived(
    actualEmail: String,
    travelerLocalDataSource: TravelerLocalDataSource,
    travelerRemoteDataSource: TravelerRemoteDataSource
) : TravelerRepository by TravelerRepositoryImpl(
    actualEmail,
    travelerLocalDataSource,
    travelerRemoteDataSource
)