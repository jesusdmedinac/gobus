package com.mupper.gobus.data.source

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.mupper.domain.LatLng

interface LocationDataSource {
    suspend fun findLastLocation(): LatLng?

    suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    )
}