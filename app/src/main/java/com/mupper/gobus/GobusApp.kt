package com.mupper.gobus

import android.app.Application
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.di.DaggerGobusComponent
import com.mupper.gobus.di.GobusComponent

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class GobusApp : Application() {

    lateinit var component: GobusComponent
        private set
    
    override fun onCreate() {
        super.onCreate()

        component = DaggerGobusComponent
            .factory()
            .create(this)
    }
}