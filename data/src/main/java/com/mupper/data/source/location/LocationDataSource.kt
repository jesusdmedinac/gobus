package com.mupper.data.source.location

import com.mupper.domain.LatLng

interface LocationDataSource<LocationRequest, LocationCallback> {
    suspend fun findLastLocation(): LatLng?

    suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    )
}