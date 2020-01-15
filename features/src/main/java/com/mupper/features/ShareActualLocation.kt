package com.mupper.features

import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler
import com.mupper.features.bus.GetTravelingBus
import com.mupper.features.traveler.GetCurrentTraveler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShareActualLocation(
    private val getTravelingBus: GetTravelingBus,
    private val getCurrentTraveler: GetCurrentTraveler,
    private val busLocalDataSource: BusLocalDataSource,
    private val busRemoteDataSource: BusRemoteDataSource
) {
    suspend fun invoke(newLatLng: LatLng) = withContext(Dispatchers.IO) {
        val travelingBus = getTravelingBus.getActualBus()
        val (email, _, isTraveling) = getCurrentTraveler.invoke()
        busLocalDataSource.shareActualLocation(travelingBus)
        busRemoteDataSource.shareActualLocation(
            travelingBus, Traveler(
                email,
                newLatLng,
                isTraveling
            )
        )
    }
}