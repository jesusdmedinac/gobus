package com.mupper.gobus.data

import com.mupper.gobus.commons.FIELDS_BUS
import com.mupper.gobus.commons.FIELD_BUS_CAPACITY
import com.mupper.gobus.commons.FIELD_BUS_COLOR
import com.mupper.gobus.commons.FIELD_BUS_PATH
import com.mupper.gobus.commons.FIELDS_TRAVELER
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION_LATITUDE
import com.mupper.gobus.commons.FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE
import com.mupper.gobus.commons.FIELD_TRAVELER_EMAIL
import com.mupper.gobus.commons.FIELD_TRAVELER_IS_TRAVELING
import com.mupper.gobus.commons.extension.hasAll
import android.location.Location as AndroidLocation
import com.google.android.gms.maps.model.LatLng as MapsLatLng
import com.mupper.domain.LatLng as DomainLatLng
import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.domain.relations.BusWithTravelers as DomainBusWithTravelers
import com.mupper.domain.traveler.Traveler as DomainTraveler
import com.mupper.gobus.data.database.CurrentPosition as RoomLatLng
import com.mupper.gobus.data.database.bus.Bus as RoomBus
import com.mupper.gobus.data.database.relations.BusWithTravelers as RoomBusWithTravelers
import com.mupper.gobus.data.database.traveler.Traveler as RoomTraveler

// Bus
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
    var currentPosition = DomainLatLng(0.0, 0.0)
    var email = ""
    if (hasAll(FIELDS_TRAVELER)) {
        val currentPositionMap =
            get(FIELD_TRAVELER_CURRENT_POSITION) as HashMap<*, *>

        currentPosition =
            DomainLatLng(
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LATITUDE] as Double? ?: 0.0,
                currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE] as Double? ?: 0.0
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

fun DomainTraveler.toRoomTraveler(travelingPath: String) = RoomTraveler(
    email,
    currentPosition.toRoomLatLng(),
    travelingPath,
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

fun DomainLatLng.toMapsLatLng() = MapsLatLng(
    latitude,
    longitude
)

fun MapsLatLng.toDomainLatLng() = DomainLatLng(
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

// List<Traveler>
fun List<DomainTraveler>.toRoomTravelerList(travelingPath: String) = map {
    RoomTraveler(
        it.email,
        it.currentPosition.toRoomLatLng(),
        travelingPath,
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
fun AndroidLocation.toDomainLatLng() = DomainLatLng(latitude, longitude)

