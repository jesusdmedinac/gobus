package com.mupper.gobus

import android.app.Application
import com.mupper.gobus.data.database.GobusDatabase
import org.koin.android.ext.android.inject

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class GobusApp : Application() {

    val db: GobusDatabase by inject()

    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}