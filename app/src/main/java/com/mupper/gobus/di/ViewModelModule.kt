package com.mupper.gobus.di

import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun mapsViewModelProvider(locationRepository: LocationRepository, busMarker: BitmapDescriptor) =
        MapsViewModel(locationRepository, busMarker)

    @Provides
    fun travelerViewModelProvider(shareActualLocation: ShareActualLocation) =
        TravelerViewModel(shareActualLocation)

    @Provides
    fun travelViewModelProvider(travelControl: TravelControl) = TravelViewModel(travelControl)

    @Provides
    fun busViewModelProvider(addNewBusWithTravelers: AddNewBusWithTravelers) =
        BusViewModel(addNewBusWithTravelers)
}