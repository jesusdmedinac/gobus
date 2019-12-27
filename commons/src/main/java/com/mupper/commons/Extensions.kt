package com.mupper.commons

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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

// Objects
// Log
fun Any.tag(): String? = this::class.java.name

fun Any.d(msg: String) {
    Log.d(tag(), msg)
}

fun Any.w(msg: String, exception: Exception) {
    Log.w(tag(), msg, exception)
}