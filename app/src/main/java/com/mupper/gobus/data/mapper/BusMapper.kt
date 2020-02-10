package com.mupper.gobus.data.mapper

import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.domain.relations.BusWithTravelers as DomainBusWithTravelers
import com.mupper.domain.traveler.Traveler as DomainTraveler
import com.mupper.gobus.data.database.bus.Bus as RoomBus

fun DomainBus.toRoomBus() = RoomBus(
    path,
    color,
    capacity,
    isTraveling
)

fun RoomBus.toDomainBus() = DomainBus(
    path,
    color,
    capacity,
    isTraveling
)

fun DomainBus.toDomainBusWithTravelers(traveler: DomainTraveler) = DomainBusWithTravelers(
    path,
    color,
    capacity,
    listOf(traveler)
)
