package com.mupper.gobus

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.data.source.resources.BusIcon
import com.mupper.data.source.resources.MapResourcesDataSource
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.commons.extension.getBitmapFromVector
import com.mupper.gobus.commons.extension.getCompatColor
import com.mupper.gobus.viewmodel.MapsViewModel
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
        modules(listOf(mockedAppModule, dataSourceModule, featureModule, mockedViewModule))
    }
}

private val mockedAppModule = module {
    factory<MapResourcesDataSource<BitmapDescriptor>> { FakeMapResourceDataSource() }
    single { Dispatchers.Unconfined }
}

private val featureModule = module {
    factory { GetActualTraveler(get(), get(), get()) }
    factory { AddNewBusWithTravelers(get(), get(), get()) }
}

private val mockedViewModule: Module = module {
    factory { MapsViewModel(get(), get(), get()) }
    factory { TravelerViewModel(get(), get()) }
    factory { TravelViewModel(get(), get()) }
}

class FakeMapResourceDataSource : MapResourcesDataSource<BitmapDescriptor> {
    override fun getBusIcon(): BitmapDescriptor =
        app.getBitmapFromVector(
            busIcon.vectorResourceId,
            busIcon.tintColor
        )

    override var busIcon = BusIcon(
        R.drawable.ic_bus_marker,
        app.getCompatColor(R.color.primaryDarkColor)
    )
}
