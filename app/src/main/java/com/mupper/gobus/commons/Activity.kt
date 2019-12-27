package com.mupper.gobus.commons

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.mupper.gobus.R


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

fun <T : ViewDataBinding> Activity.bindingInflate(
    @LayoutRes layoutRes: Int
): T =
    DataBindingUtil.setContentView(this, R.layout.activity_main)

fun Activity.getCompatDrawable(drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}

fun Activity.getCompatColor(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}