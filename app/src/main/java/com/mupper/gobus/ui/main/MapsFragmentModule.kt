package com.mupper.gobus.ui.main

import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.firestore.TravelerRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.bus.GetTravelingBus
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MapsFragmentModule {
    @Provides
    fun mapsViewModelProvider(locationRepository: LocationRepository, busMarker: BitmapDescriptor) =
        MapsViewModel(locationRepository, busMarker)

    @Provides
    fun travelViewModelProvider(travelControl: TravelControl) = TravelViewModel(travelControl)

    @Provides
    fun travelerViewModelProvider(shareActualLocation: ShareActualLocation) =
        TravelerViewModel(shareActualLocation)

    @Provides
    fun busViewModelProvider(addNewBusWithTravelers: AddNewBusWithTravelers) =
        BusViewModel(addNewBusWithTravelers)

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

    @Provides
    fun getTravelingBusProvider(
        busLocalDataSource: BusLocalDataSource,
        busRemoteDataSource: BusRemoteDataSource
    ) = GetTravelingBus(busLocalDataSource, busRemoteDataSource)

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
    fun getActualTravelerProvider(
        travelerLocalDataSource: TravelerLocalDataSource,
        travelerRemoteDataSource: TravelerRemoteDataSource
    ) = GetActualTraveler(
        travelerLocalDataSource,
        travelerRemoteDataSource
    )
}

@Subcomponent(modules = [(MapsFragmentModule::class)])
interface MapsFragmentComponent {
    val mapsViewModel: MapsViewModel
    val travelViewModel: TravelViewModel
    val travelerViewModel: TravelerViewModel
    val busViewModel: BusViewModel
}