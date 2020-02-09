package com.mupper.features.bus

import com.mupper.data.repository.BusRepository
import com.mupper.domain.relations.BusWithTravelers

class GetActualBusWithTravelers(
    private val busRepository: BusRepository
) {
    suspend fun invoke(): BusWithTravelers? {
        val busWithTravelers: List<BusWithTravelers> = busRepository.getTravelingBusWithTravelers()
        if (busWithTravelers.isEmpty()) {
            return null
        }

        return busWithTravelers[0]
    }
}