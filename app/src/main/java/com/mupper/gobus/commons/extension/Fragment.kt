package com.mupper.gobus.commons.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.mupper.gobus.GobusApp


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */

val Fragment.app: GobusApp
    get() = ((activity?.app)
        ?: IllegalStateException("Fragment needs to be attach to the activity to access the App instance"))
            as GobusApp

val Fragment.supportFragmentManager: FragmentManager
    get() = ((activity?.supportFragmentManager)
        ?: java.lang.IllegalStateException("Fragment needs to be attach to the activity to access the App instance"))
            as FragmentManager

fun Fragment.navController() = view?.findNavController()

fun Fragment.navigate(directions: NavDirections) {
    navController()?.navigate(directions)
}