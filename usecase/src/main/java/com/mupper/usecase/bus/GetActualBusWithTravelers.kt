package com.mupper.usecase.bus

import com.mupper.data.repository.BusRepository
import com.mupper.domain.relations.BusWithTravelers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetActualBusWithTravelers(
    private val busRepository: BusRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun invoke(): BusWithTravelers? {
        val busWithTravelers: List<BusWithTravelers> = withContext(dispatcher) {
            busRepository.getTravelingBusWithTravelers()
        }
        if (busWithTravelers.isEmpty()) {
            return null
        }

        return busWithTravelers[0]
    }
}