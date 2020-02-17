package com.mupper.gobus

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val app: GobusApp = getApplicationContext()

fun initMockedDi() {
    stopKoin()
    startKoin {
        androidContext(app)
        modules(
            listOf(
                mockedAppModule,
                dataSourceModule,
                repositoryModule,
                featureModule,
                mockedViewModule
            )
        )
    }
}

private val mockedAppModule = module {
    single { Dispatchers.Unconfined }
}

private val featureModule = module {
    factory { GetActualTraveler(get()) }
    factory { AddNewBusWithTravelers(get(), get(), get()) }
}

private val mockedViewModule: Module = module {
    factory { MapViewModel(get(), get(), get()) }
    factory { TravelerViewModel(get(), get()) }
    factory { TravelViewModel(get(), get()) }
}