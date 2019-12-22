package com.mupper.gobus

import android.app.Application
import com.mupper.core.database.GobusDatabase


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class GobusApp : Application() {

    lateinit var db: GobusDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        db = GobusDatabase.getInstance(this)
    }
}