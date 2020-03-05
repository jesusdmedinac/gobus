package com.mupper.gobus.model

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.mupper.data.source.resources.TravelControlDataSource
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.getCompatColor
import com.mupper.gobus.commons.extension.getCompatDrawable

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
data class TravelControl(
    val context: Context
) : TravelControlDataSource<Drawable?> {
    override val playIcon = context.getCompatDrawable(R.drawable.ic_start)
    override val stopIcon = context.getCompatDrawable(R.drawable.ic_stop)
    @ColorInt
    override val defaultFabColor = context.getCompatColor(R.color.secondaryColor)
    @ColorInt
    override val defaultFabIconColor = context.getCompatColor(R.color.secondaryTextColor)
}
