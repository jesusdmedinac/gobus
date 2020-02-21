package com.mupper.gobus.data.source.resources

import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import androidx.annotation.VisibleForTesting
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.data.repository.MapResourcesRepository
import com.mupper.data.source.resources.MapMarkerDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.commons.MILLISECONDS_SIXTEEN_MILLIS
import com.mupper.gobus.commons.MILLISECONDS_TEN_MILLIS
import com.mupper.gobus.data.mapper.toDomainLatLng
import com.mupper.gobus.data.mapper.toMapsLatLng

class TravelerMapMarkerDataSource(
    private val mapResourcesRepository: MapResourcesRepository<BitmapDescriptor>
) : MapMarkerDataSource<Marker, MarkerOptions> {
    override var mapMarker: Marker? = null
    override var mapMarkerOptions: MarkerOptions? = null
    override var visibleForMap: Boolean = false

    override fun moveMarkerToLastLocation(
        enableUserLocation: (Boolean) -> Unit,
        lastLatLng: LatLng,
        addMarkerToMap: (MarkerOptions) -> Marker?
    ): LatLng {
        val mapsLatLng = lastLatLng.toMapsLatLng()
        if (mapMarkerOptions == null || mapMarker == null) {
            mapMarkerOptions = MarkerOptions().apply {
                position(mapsLatLng)?.title("User position")
                icon(mapResourcesRepository.getBusIcon())
                addMarkerToMap(this)?.let {
                    mapMarker = it
                }
            }
        } else {
            mapMarkerOptions?.position(mapsLatLng)?.let {
                mapMarkerOptions = it
            }
            mapMarker?.position = mapsLatLng
        }

        shouldDrawMarker(enableUserLocation) {
            smoothMoveMarker(lastLatLng)
        }

        return lastLatLng
    }

    private fun shouldDrawMarker(enableUserLocation: (Boolean) -> Unit, markerAction: () -> Unit) {
        mapMarker?.isVisible = visibleForMap
        enableUserLocation(!visibleForMap)
        if (visibleForMap) {
            markerAction()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun smoothMoveMarker(finalLatLng: LatLng) {
        val startLatLng = mapMarker?.position?.toDomainLatLng() ?: LatLng(0.0, 0.0)

        val start = getSystemClockUptimeMillis()

        val duration = MILLISECONDS_TEN_MILLIS
        val interpolator = LinearInterpolator()

        val handler = getHandler()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = getSystemClockUptimeMillis() - start
                val t = interpolator.getInterpolation((elapsed / duration).toFloat())

                val lat = t * finalLatLng.latitude + (1 - t) * startLatLng.latitude
                val lng = t * finalLatLng.longitude + (1 - t) * startLatLng.longitude

                mapMarker?.position = LatLng(lat, lng).toMapsLatLng()

                val timeElapsed = 1.0
                if (t < timeElapsed) {
                    handler.postDelayed(this, MILLISECONDS_SIXTEEN_MILLIS)
                }
            }
        })
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getSystemClockUptimeMillis() = SystemClock.uptimeMillis()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getHandler() = Handler()
}