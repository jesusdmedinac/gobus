package com.mupper.gobus.model

import android.content.Context
import com.mupper.gobus.R
import com.mupper.gobus.commons.getCompatColor
import com.mupper.gobus.commons.getCompatDrawable


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
data class TravelControl(
    val context: Context
) {
    val playIcon = context.getCompatDrawable(R.drawable.ic_start)
    val stopIcon = context.getCompatDrawable(R.drawable.ic_stop)
    val defaultFabColor = context.getCompatColor(R.color.colorAccent)
    val defaultFabIconColor = context.getCompatColor(R.color.white)
}