package com.mupper.gobus.repository

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.mupper.gobus.model.PermissionChecker
import com.mupper.gobus.model.PlayServicesLocationDataSource
import com.mupper.gobus.model.UserLocation

class UserLocationRepository(val activity: Activity) {

    private val locationDataSource = PlayServicesLocationDataSource(activity)
    private val coarseLocationPermissionChecker = PermissionChecker(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val fineLocationPermissionChecker = PermissionChecker(activity, Manifest.permission.ACCESS_FINE_LOCATION)

    suspend fun findLastLocation(): UserLocation? {
        val hasCoarseLocation = coarseLocationPermissionChecker.request()
        val hasFineLocation = fineLocationPermissionChecker.request()
        if (!isLocationEnabled()) {
            return null
        }
        if (!hasCoarseLocation || !hasFineLocation) {
            return null
        }
        return UserLocation(locationDataSource.findLastLocation())
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}