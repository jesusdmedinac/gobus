package com.mupper.features.bus

import com.mupper.data.source.room.BusLocalDataSource

class IsBusInDb(
    private val busLocalDataSource: BusLocalDataSource,
    private val path: String
) {
    suspend fun invoke() = busLocalDataSource.getCount(path)
}