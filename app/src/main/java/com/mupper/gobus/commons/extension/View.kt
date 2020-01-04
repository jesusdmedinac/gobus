package com.mupper.gobus.commons.extension

import android.app.Activity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.mupper.gobus.R


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */

fun Activity.navigate(directions: NavDirections) {
    val navController = this.findNavController(R.id.main_nav_host_fragment)
    navController.navigate(directions)
}