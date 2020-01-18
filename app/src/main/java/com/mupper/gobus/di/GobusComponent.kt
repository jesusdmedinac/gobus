package com.mupper.gobus.di

import android.app.Application
import com.mupper.gobus.ui.main.MapsFragmentComponent
import com.mupper.gobus.ui.main.MapsFragmentModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface GobusComponent {

    fun plus(module: MapsFragmentModule): MapsFragmentComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): GobusComponent
    }
}