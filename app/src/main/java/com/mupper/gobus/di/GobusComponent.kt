package com.mupper.gobus.di

import android.app.Application
import com.mupper.gobus.data.database.GobusDatabaseComponent
import com.mupper.gobus.ui.bus.NewBusFragmentComponent
import com.mupper.gobus.ui.main.MapsFragmentComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, FeatureModule::class, ViewModelModule::class])
interface GobusComponent {

    fun mapsFragmentComponent(): MapsFragmentComponent
    fun newBusFragmentComponent(): NewBusFragmentComponent
    fun gobusDatabaseComponent(): GobusDatabaseComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): GobusComponent
    }
}