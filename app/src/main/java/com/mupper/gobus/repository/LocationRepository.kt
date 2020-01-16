package com.mupper.gobus.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.mupper.gobus.model.PermissionChecker
import com.mupper.gobus.model.PlayServicesLocationDataSource
import com.mupper.gobus.model.Location

class LocationRepository(val app: Application) {

    private val locationDataSource = PlayServicesLocationDataSource(app)
    private val fineLocationPermissionChecker =
        PermissionChecker(app, Manifest.permission.ACCESS_FINE_LOCATION)

    suspend fun findLastLocation(): Location? {
        if (!canAccessLocation()) {
            return null
        }
        return Location(locationDataSource.findLastLocation())
    }

    suspend fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
        if (!canAccessLocation()) {
            return
        }
        locationDataSource.requestLocationUpdates(locationRequest, locationCallback)
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