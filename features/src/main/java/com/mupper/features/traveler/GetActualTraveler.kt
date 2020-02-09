package com.mupper.features.traveler

import com.mupper.data.repository.TravelerRepository
import com.mupper.domain.traveler.Traveler

class GetActualTraveler(
    private val travelerRepository: TravelerRepository
) {
    suspend fun invoke(travelingPath: String): Traveler? =
        travelerRepository.retrieveActualTraveler(travelingPath)
}