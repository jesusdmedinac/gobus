package com.mupper.usecase

import com.mupper.data.repository.BusRepository
import com.mupper.data.repository.TravelerRepository
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler
import com.mupper.usecase.bus.GetActualBusWithTravelers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ShareActualLocation(
    private val getActualBusWithTravelers: GetActualBusWithTravelers,
    private val travelerRepository: TravelerRepository,
    private val busRepository: BusRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun invoke(newLatLng: LatLng) = withContext(dispatcher) {
        val travelingBus = withContext(dispatcher) { getActualBusWithTravelers.invoke() }
        travelingBus?.let {
            val travelersInBus = it.travelers
            if (travelersInBus.isNullOrEmpty()) {
                return@withContext
            }
            val travelerInBus = travelersInBus[0]
            travelerInBus.isTraveling = true
            travelerInBus.currentPosition = newLatLng
            withContext(dispatcher) {
                busRepository.shareActualLocation(
                    travelingBus, Traveler(
                        travelerInBus.email,
                        newLatLng,
                        true
                    )
                )
            }
            withContext(dispatcher) { travelerRepository.shareActualLocation(travelerInBus) }
        }
    }
}