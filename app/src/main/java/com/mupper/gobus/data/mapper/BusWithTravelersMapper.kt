package com.mupper.gobus.data.mapper


import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.domain.relations.BusWithTravelers as DomainBusWithTravelers
import com.mupper.gobus.data.database.bus.Bus as RoomBus
import com.mupper.gobus.data.database.relations.BusWithTravelers as RoomBusWithTravelers

fun DomainBusWithTravelers.toRoomBusWithTravelers() = RoomBusWithTravelers(
    RoomBus(
        path,
        color,
        capacity,
        travelers.isNotEmpty()
    ),
    travelers.toRoomTravelerList(path)
)

fun RoomBusWithTravelers.toDomainBusWithTravelers() = DomainBusWithTravelers(
    bus.path,
    bus.color,
    bus.capacity,
    travelers.toDomainTravelerList()
)

fun DomainBusWithTravelers.toDomainBus() = DomainBus(
    path,
    color,
    capacity,
    travelers.isNotEmpty()
)

fun DomainBusWithTravelers.toRoomBus() = RoomBus(
    path,
    color,
    capacity,
    travelers.isNotEmpty()
)
