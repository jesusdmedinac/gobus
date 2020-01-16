package com.mupper.gobus.model

import android.app.Application
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface LocationDataSource {
    suspend fun findLastLocation(): Location?

    suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    )
}

class PlayServicesLocationDataSource(app: Application) : LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)

    override suspend fun findLastLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        continuation.resume(it.result)
                    } else {
                        continuation.resume(Location("dummyprovider"))
                    }
                }
        }

    override suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}
