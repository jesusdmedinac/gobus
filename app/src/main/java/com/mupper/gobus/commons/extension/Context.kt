package com.mupper.gobus.commons.extension

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.mupper.gobus.GobusApp

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
val Context.app: GobusApp
    get() = applicationContext as GobusApp

fun Context.getCompatDrawable(@DrawableRes drawableId: Int): Drawable? =
    ContextCompat.getDrawable(this, drawableId)

fun Context.getCompatColor(@ColorRes colorId: Int): Int =
    ContextCompat.getColor(this, colorId)

fun Application.getBitmapFromVector(
    @DrawableRes vectorResourceId: Int,
    @ColorInt tintColor: Int
): BitmapDescriptor {
    val vectorDrawable = ResourcesCompat.getDrawable(this.applicationContext.resources, vectorResourceId, null)
        ?: return BitmapDescriptorFactory.defaultMarker()
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    DrawableCompat.setTint(vectorDrawable, tintColor)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
