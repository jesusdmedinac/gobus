package com.mupper.gobus.commons

import android.content.Context
import androidx.fragment.app.Fragment
import com.mupper.gobus.GobusApp


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
val Context.app: GobusApp
    get() = applicationContext as GobusApp
