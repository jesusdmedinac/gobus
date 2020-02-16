package com.mupper.gobus.data.source.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.location.Location as AndroidLocation
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.mupper.data.source.location.LocationDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.data.mapper.toDomainLatLng
import com.mupper.gobus.model.PermissionChecker
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource(val app: Application) :
    LocationDataSource<LocationRequest, LocationCallback> {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(app)
    private val fineLocationPermissionChecker =
        PermissionChecker(app, Manifest.permission.ACCESS_FINE_LOCATION)

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

    override suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        if (!canAccessLocation()) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun isLocationPermissionAllowed(): Boolean = fineLocationPermissionChecker.check()

    private fun canAccessLocation(): Boolean = isLocationEnabled() && isLocationPermissionAllowed()
}
