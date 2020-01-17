package com.mupper.gobus

import android.app.Application
import com.mupper.gobus.data.database.GobusDatabase

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class GobusApp : Application() {

    lateinit var db: GobusDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = GobusDatabase.getInstance(this)
    }
}