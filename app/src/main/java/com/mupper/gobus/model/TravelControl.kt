package com.mupper.gobus.model

import android.content.Context
import androidx.annotation.ColorInt
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.getCompatColor
import com.mupper.gobus.commons.extension.getCompatDrawable

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
data class TravelControl(
    val context: Context
) {
    val playIcon = context.getCompatDrawable(R.drawable.ic_start)
    val stopIcon = context.getCompatDrawable(R.drawable.ic_stop)
    @ColorInt val defaultFabColor = context.getCompatColor(R.color.secondaryColor)
    @ColorInt val defaultFabIconColor = context.getCompatColor(R.color.secondaryTextColor)
}
