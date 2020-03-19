package com.mupper.sharedtestcode

import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.domain.relations.BusWithTravelers as DomainBusWithTravelers

const val fakeTravelingPath = "Ruta 2"

val fakeBus = DomainBus(fakeTravelingPath, "color", 1, false)

val fakeBusWithTravelers = DomainBusWithTravelers(fakeTravelingPath, "color", 1, emptyList())