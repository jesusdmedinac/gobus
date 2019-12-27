package com.mupper.gobus.commons

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.mupper.gobus.GobusApp


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */

val Fragment.app: GobusApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attach to the activity to access the App instance"))
            as GobusApp

val Fragment.supportFragmentManager: FragmentManager
    get() = ((activity?.supportFragmentManager)
        ?: java.lang.IllegalStateException("Fragment needs to be attach to the activity to access the App instance"))
            as FragmentManager

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.getViewModel(crossinline factory: () -> T): T {

    val vmFactory = object : ViewModelProvider.Factory {
        override fun <U : ViewModel> create(modelClass: Class<U>): U = factory() as U
    }

    return ViewModelProviders.of(requireActivity(), vmFactory)[T::class.java]
}

fun Fragment.navigate(directions: NavDirections) {
    val navController = view?.findNavController()
    navController?.navigate(directions)
}

fun Fragment.getCompatDrawable(drawableId: Int): Drawable? {
    requireContext().let {
        return ContextCompat.getDrawable(requireContext(), drawableId)
    }
}

fun Fragment.getCompatColor(colorId: Int): Int {
    requireContext().let {
        return ContextCompat.getColor(requireContext(), colorId)
    }
}