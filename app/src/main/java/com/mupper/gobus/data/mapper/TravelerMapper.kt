package com.mupper.gobus.data.mapper

import android.util.Log
import com.mupper.domain.traveler.Traveler as DomainTraveler
import com.mupper.gobus.data.database.traveler.Traveler as RoomTraveler

fun RoomTraveler.toDomainTraveler() = DomainTraveler(
    email,
    currentPosition.toDomainLatLng(),
    isTraveling
)

fun DomainTraveler.toRoomTraveler(travelingPath: String): RoomTraveler {
    return RoomTraveler(
        email,
        currentPosition.toRoomLatLng(),
        travelingPath,
        isTraveling
    )
}

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
