package com.mupper.sharedtestcode

import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers

const val fakeTravelingPath = "Ruta 2"

val fakeBus = Bus(fakeTravelingPath, "color", 1, false)

val fakeBusWithTravelers = BusWithTravelers(fakeTravelingPath, "color", 1, emptyList())