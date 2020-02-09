package com.mupper.gobus.data.source.resources

import android.app.Application
import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.data.source.resources.MapResourcesDataSource
import com.mupper.domain.resources.BusIcon
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.getBitmapFromVector
import com.mupper.gobus.commons.extension.getCompatColor

class MapBitmapDescriptorDataSource(val app: Application) :
    MapResourcesDataSource<BitmapDescriptor> {
    override fun getBusIcon(): BitmapDescriptor =
        app.getBitmapFromVector(
            busIcon.vectorResourceId,
            busIcon.tintColor
        )

    override var busIcon = BusIcon(
        R.drawable.ic_bus_marker,
        app.getCompatColor(R.color.primaryDarkColor)
    )
}