package com.mupper.gobus.commons.extension

import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */

fun DialogFragment.navigate(directions: NavDirections) {
    val navController = this.findNavController()
    navController.navigate(directions)
}