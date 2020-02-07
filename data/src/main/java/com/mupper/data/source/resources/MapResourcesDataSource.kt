package com.mupper.data.source.resources

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class BusIcon(
    @DrawableRes val vectorResourceId: Int,
    @ColorInt val tintColor: Int
)

interface MapResourcesDataSource<B> {
    var busIcon: BusIcon

    fun getBusIcon(): B
}