package com.mupper.data.repository

import com.mupper.data.source.resources.MapResourcesDataSource

interface MapResourcesRepository<B> {
    fun getBusIcon(): B?
}

private class MapResourcesRepositoryImpl<B>(
    private val mapResourcesDataSource: MapResourcesDataSource<B>
) : MapResourcesRepository<B> {
    override fun getBusIcon(): B? = mapResourcesDataSource.getBusIcon()
}

class MapResourcesRepositoryDerived<B>(
    mapResourcesDataSource: MapResourcesDataSource<B>
) : MapResourcesRepository<B> by MapResourcesRepositoryImpl(mapResourcesDataSource)
