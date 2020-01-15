package com.mupper.features.traveler

import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.domain.traveler.Traveler

class GetCurrentTraveler(
    private val travelerLocalDataSource: TravelerLocalDataSource
) {
    fun invoke(): Traveler = travelerLocalDataSource.getActualTraveler()
}