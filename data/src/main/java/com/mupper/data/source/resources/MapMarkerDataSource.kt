package com.mupper.data.source.resources

import com.mupper.domain.LatLng

interface MapMarkerDataSource<Marker, MarkerOptions> {
    var mapMarker: Marker?
    var mapMarkerOptions: MarkerOptions?
    var visibleForMap: Boolean

    fun moveMarkerToLastLocation(
        enableUserLocation: (Boolean) -> Unit,
        lastLatLng: LatLng,
        addMarkerToMap: (MarkerOptions) -> Marker?
    ): LatLng

    fun clearMarker() {
        mapMarker = null
        mapMarkerOptions = null
    }
}
