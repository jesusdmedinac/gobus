package com.mupper.sharedtestcode

import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers

const val mockedTravelingPath = "Ruta 2"

val mockedBus = Bus(mockedTravelingPath, "color", 1, false)

val mockedBusWithTravelers = BusWithTravelers(mockedTravelingPath, "color", 1, emptyList())