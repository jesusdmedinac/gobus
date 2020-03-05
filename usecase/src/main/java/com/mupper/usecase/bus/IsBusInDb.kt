package com.mupper.usecase.bus

import com.mupper.data.source.local.BusLocalDataSource

class IsBusInDb(
    private val busLocalDataSource: BusLocalDataSource,
    private val path: String
) {
    suspend fun invoke() = busLocalDataSource.getCount(path)
}