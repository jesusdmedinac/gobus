package com.mupper.commons

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.Exception


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(this, vmFactory)[T::class.java]
}

// Activity
fun Activity.getCompatDrawable(drawableId: Int) = ContextCompat.getDrawable(this, drawableId)

fun Activity.getCompatColor(colorId: Int) = ContextCompat.getColor(this, colorId)

// Objects
fun Any.tag(): String? = this::class.java.name


fun Any.d(msg: String) {
    Log.d(tag(), msg)
}

fun Any.w(msg: String, exception: Exception) {
    Log.w(tag(), msg, exception)
}