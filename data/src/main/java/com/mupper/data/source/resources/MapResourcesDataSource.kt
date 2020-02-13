package com.mupper.data.source.resources

import com.mupper.domain.resources.BusIcon

interface MapResourcesDataSource<B> {
    var busIcon: BusIcon

    fun getBusIcon(): B
}