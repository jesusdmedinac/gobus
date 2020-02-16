package com.mupper.features.bus

import com.mupper.data.repository.BusRepository
import com.mupper.domain.bus.Bus
import com.mupper.features.traveler.GetActualTraveler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddNewBusWithTravelers(
    private val getActualTraveler: GetActualTraveler,
    private val busRepository: BusRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun invoke(bus: Bus) = withContext(dispatcher) {
        val traveler = getActualTraveler.invoke(bus.path)
        traveler?.let {
            busRepository.addNewBusWithTravelers(bus, traveler)
        }
    }
}