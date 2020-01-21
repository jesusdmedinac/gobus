package com.mupper.gobus.di

import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.firestore.TravelerRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.bus.GetTravelingBus
import com.mupper.features.traveler.GetActualTraveler
import dagger.Module
import dagger.Provides

@Module
class FeatureModule {
    @Provides
    fun getActualTravelerProvider(
        travelerLocalDataSource: TravelerLocalDataSource,
        travelerRemoteDataSource: TravelerRemoteDataSource
    ) = GetActualTraveler(
        travelerLocalDataSource,
        travelerRemoteDataSource
    )

    @Provides
    fun addNewBusWithTravelersProvider(
        getActualTraveler: GetActualTraveler,
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ) = AddNewBusWithTravelers(
        getActualTraveler,
        busLocalDataSource,
        busRemoteDataSource
    )

    @Provides
    fun getTravelingBusProvider(
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ) = GetTravelingBus(busLocalDataSource, busRemoteDataSource)

    @Provides
    fun shareActualLocationProvider(
        getTravelingBus: GetTravelingBus,
        travelerLocalDataSource: TravelerLocalDataSource,
        travelerRemoteDataSource: TravelerRemoteDataSource,
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ): ShareActualLocation = ShareActualLocation(
        getTravelingBus,
        travelerLocalDataSource,
        travelerRemoteDataSource,
        busLocalDataSource,
        busRemoteDataSource
    )
}