package com.mupper.gobus.data.source.location

import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.mupper.data.source.location.LocationDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.data.mapper.toDomainLatLng
import com.mupper.gobus.model.PermissionChecker
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import android.location.Location as AndroidLocation

class PlayServicesLocationDataSource(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val fineLocationPermissionChecker: PermissionChecker,
    private val locationManager: LocationManager,
    private val mainLooper: Looper
) :
    LocationDataSource<LocationRequest, LocationCallback> {

    override suspend fun findLastLocation(): LatLng? {
        if (!canAccessLocation()) {
            return null
        }
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        continuation.resume(AndroidLocation(it.result).toDomainLatLng())
                    } else {
                        continuation.resume(AndroidLocation("dummyprovider").toDomainLatLng())
                    }
                }
        }
    }

    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        if (!canAccessLocation()) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            mainLooper
        )
    }

    private fun isLocationEnabled(): Boolean = locationManager
        .isProviderEnabled(LocationManager.GPS_PROVIDER)
            || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    private fun isLocationPermissionAllowed(): Boolean = fineLocationPermissionChecker.check()

    private fun canAccessLocation(): Boolean = isLocationEnabled() && isLocationPermissionAllowed()
}
