package com.mupper.gobus.data

import com.google.type.LatLng
import com.mupper.commons.*
import com.mupper.gobus.commons.extension.hasAll
import android.location.Location as AndroidLocation
import com.mupper.gobus.data.database.bus.Bus as RoomBus
import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.gobus.data.database.traveler.Traveler as RoomTraveler
import com.mupper.domain.traveler.Traveler as DomainTraveler
import com.mupper.gobus.data.database.CurrentPosition as RoomLatLng
import com.mupper.domain.LatLng as DomainLatLng
import com.mupper.gobus.data.database.relations.BusWithTravelers as RoomBusWithTravelers
import com.mupper.domain.relations.BusWithTravelers as DomainBusWithTravelers

// Bus
fun DomainBus.toRoomBus() = RoomBus(
    path,
    color,
    capacity,
    isTraveling
)

fun RoomBus.toDomainBus(travelers: List<DomainTraveler>) = DomainBus(
    path,
    color,
    capacity,
    isTraveling
)

fun Map<String, Any>.toDomainBus(): DomainBus {
    var busPath = ""
    var busCapacity = 0
    var busColor = ""
    if (hasAll(FIELDS_BUS)) {
        busPath = get(FIELD_BUS_PATH) as String
        busCapacity = get(FIELD_BUS_CAPACITY) as Int
        busColor = get(FIELD_BUS_COLOR) as String
    }

    return DomainBus(
        busPath,
        busColor,
        busCapacity,
        false
    )
}

fun Map<String, Any>.toDomainTraveler(): DomainTraveler {
    var currentPosition = DomainLatLng(0f, 0f)
    var email = ""
    if (hasAll(FIELDS_TRAVELER)) {
        val currentPositionMap =
            get(FIELD_TRAVELER_CURRENT_POSITION) as HashMap<*, *>

        currentPosition =
            DomainLatLng(
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LATITUDE] as Float? ?: 0f,
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE] as Float? ?: 0f
            )
        email = get(FIELD_TRAVELER_EMAIL) as String
    }
    var isTraveling = false
    if (this.containsKey(FIELD_TRAVELER_IS_TRAVELING)) {
        isTraveling = get(FIELD_TRAVELER_IS_TRAVELING) as Boolean
    }

    return DomainTraveler(
        email,
        currentPosition,
        isTraveling
    )
}

// Traveler
fun RoomTraveler.toDomainTraveler() = DomainTraveler(
    email,
    currentPosition.toDomainLatLng(),
    isTraveling
)

fun DomainTraveler.toRoomTraveler() = RoomTraveler(
    email,
    currentPosition.toRoomLatLng(),
    isTraveling
)

// LatLng
fun RoomLatLng.toDomainLatLng() = DomainLatLng(
    latitude,
    longitude
)

fun DomainLatLng.toRoomLatLng() = RoomLatLng(
    latitude,
    longitude
)

// BusWithTravelers
fun DomainBusWithTravelers.toRoomBusWithTravelers() = RoomBusWithTravelers(
    RoomBus(
        path,
        color,
        capacity,
        travelers.isNotEmpty()
    ),
    travelers.toRoomTravelerList()
)

fun RoomBusWithTravelers.toDomainBusWithTravelers() = DomainBusWithTravelers(
    bus.path,
    bus.color,
    bus.capacity,
    travelers.toDomainTravelerList()
)

// List<Traveler>
fun List<DomainTraveler>.toRoomTravelerList() = map {
    RoomTraveler(
        it.email,
        it.currentPosition.toRoomLatLng(),
        it.isTraveling
    )
}

fun List<RoomTraveler>.toDomainTravelerList() = map {
    DomainTraveler(
        it.email,
        it.currentPosition.toDomainLatLng(),
        it.isTraveling
    )
}

// AndroidLocation
fun AndroidLocation.toDomainLatLng() = DomainLatLng(latitude.toFloat(), longitude.toFloat())