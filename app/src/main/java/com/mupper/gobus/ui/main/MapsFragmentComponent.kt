package com.mupper.gobus.ui.main

import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import dagger.Subcomponent

@Subcomponent
interface MapsFragmentComponent {
    val mapsViewModel: MapsViewModel
    val travelViewModel: TravelViewModel
    val travelerViewModel: TravelerViewModel
}