package com.mupper.features.traveler

import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler

class GetActualTraveler(
    private val travelerLocalDataSource: TravelerLocalDataSource,
    private val travelerRemoteDataSource: TravelerRemoteDataSource
) {
    private val actualEmail = "dmc12345628@gmail.com"

    suspend fun invoke(travelingPath: String): Traveler? {
        if (travelerLocalDataSource.getCount() == 0) {
            val remoteTraveler = travelerRemoteDataSource.findTravelerByEmail(actualEmail)
                ?: addStaticTraveler()

            travelerLocalDataSource.insertTraveler(travelingPath, remoteTraveler)
        }
        return travelerLocalDataSource.findTravelerByEmail(actualEmail)
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