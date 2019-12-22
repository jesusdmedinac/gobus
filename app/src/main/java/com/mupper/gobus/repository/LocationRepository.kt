package com.mupper.gobus.repository

import android.Manifest
import android.app.Application
import android.content.Context
import android.location.LocationManager
import com.mupper.gobus.model.PermissionChecker
import com.mupper.gobus.model.PlayServicesLocationDataSource
import com.mupper.gobus.model.Location

class LocationRepository(val app: Application) {

    private val locationDataSource = PlayServicesLocationDataSource(app)
    private val coarseLocationPermissionChecker = PermissionChecker(app, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val fineLocationPermissionChecker = PermissionChecker(app, Manifest.permission.ACCESS_FINE_LOCATION)

    suspend fun findLastLocation(): Location? {
        val hasCoarseLocation = coarseLocationPermissionChecker.check()
        val hasFineLocation = fineLocationPermissionChecker.check()
        if (!isLocationEnabled()) {
            return null
        }
        if (!hasCoarseLocation || !hasFineLocation) {
            return null
        }
        return Location(locationDataSource.findLastLocation())
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}