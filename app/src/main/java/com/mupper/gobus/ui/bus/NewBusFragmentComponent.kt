package com.mupper.gobus.ui.bus

import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import dagger.Subcomponent

@Subcomponent
interface NewBusFragmentComponent {
    val busViewModel: BusViewModel
    val travelViewModel: TravelViewModel
}