package com.mupper.commons

import android.util.Log


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */

// Objects
// Log
fun Any.tag(): String? = this::class.java.name

fun Any.d(msg: String) {
    Log.d(tag(), msg)
}

fun Any.w(msg: String, exception: Exception) {
    Log.w(tag(), msg, exception)
}