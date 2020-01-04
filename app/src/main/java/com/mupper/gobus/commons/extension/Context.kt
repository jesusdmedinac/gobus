package com.mupper.gobus.commons.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.mupper.gobus.GobusApp


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
val Context.app: GobusApp
    get() = applicationContext as GobusApp


fun Context.getCompatDrawable(drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

fun Context.getCompatColor(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}
