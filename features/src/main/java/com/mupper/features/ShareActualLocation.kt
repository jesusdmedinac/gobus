package com.mupper.features

import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler
import com.mupper.features.bus.GetTravelingBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShareActualLocation(
    private val getTravelingBus: GetTravelingBus,
    private val travelerLocalDataSource: TravelerLocalDataSource,
    private val travelerRemoteDataSource: TravelerRemoteDataSource,
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun invoke(newLatLng: LatLng) = withContext(Dispatchers.IO) {
        val travelingBus = getTravelingBus.getActualBusWithTravelers()
        travelingBus?.let {
            val travelersInBus = it.travelers
            if (travelersInBus.isNullOrEmpty()) {
                return@withContext
            }
            val travelerInBus = travelersInBus[0]
            travelerInBus.isTraveling = true
            travelerInBus.currentPosition = newLatLng
            busLocalDataSource.shareActualLocation(travelingBus)
            busRemoteDataSource.shareActualLocation(
                travelingBus, Traveler(
                    travelerInBus.email,
                    newLatLng,
                    true
                )
            )
            travelerLocalDataSource.shareActualLocation(travelerInBus)
            travelerRemoteDataSource.shareActualLocation(travelerInBus)
        }
    }
}