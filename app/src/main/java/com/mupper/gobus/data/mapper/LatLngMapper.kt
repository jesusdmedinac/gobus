package com.mupper.gobus.data.mapper

import com.mupper.domain.LatLng as DomainLatLng
import com.mupper.gobus.data.database.CurrentPosition
import com.google.android.gms.maps.model.LatLng as MapsLatLng
import android.location.Location as AndroidLocation

fun CurrentPosition.toDomainLatLng() = DomainLatLng(
    latitude,
    longitude
)

fun DomainLatLng.toRoomLatLng() = CurrentPosition(
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

fun AndroidLocation.toDomainLatLng() = DomainLatLng(
    latitude,
    longitude
)
