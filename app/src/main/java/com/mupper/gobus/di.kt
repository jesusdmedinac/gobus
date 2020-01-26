package com.mupper.gobus

import android.app.Application
import android.content.Context
import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.firestore.TravelerRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.bus.GetTravelingBus
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.commons.extension.getBitmapFromVector
import com.mupper.gobus.commons.extension.getCompatColor
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.source.bus.BusFirebaseDataSource
import com.mupper.gobus.data.source.bus.BusRoomDataSource
import com.mupper.gobus.data.source.traveler.TravelerFirebaseDataSource
import com.mupper.gobus.data.source.traveler.TravelerRoomDataSource
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.ui.main.MapsFragment
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, featureModule, viewModelModule))
    }
}

private val appModule = module {
    single { GobusDatabase.getInstance(get()) }
    factory {
        val app = get() as Application
        app.applicationContext.resources.getBitmapFromVector(
            R.drawable.ic_bus_marker,
            app.getCompatColor(R.color.primaryDarkColor)
        )
    }
}

private val dataModule = module {
    factory { TravelControl(get()) }
    factory { LocationRepository(get()) }
    factory<BusLocalDataSource> { BusRoomDataSource(get())}
    factory<BusRemoteDataSource> { BusFirebaseDataSource() }
    factory<TravelerLocalDataSource> { TravelerRoomDataSource(get()) }
    factory<TravelerRemoteDataSource> { TravelerFirebaseDataSource() }
}

private val featureModule = module {
    factory { GetActualTraveler(get(), get()) }
    factory { AddNewBusWithTravelers(get(), get(), get()) }
    factory { GetTravelingBus(get(), get()) }
    factory { ShareActualLocation(get(), get(), get(), get(), get()) }
}

private val viewModelModule = module {
    factory { MapsViewModel(get(), get()) }
    factory { TravelerViewModel(get()) }
    factory { TravelViewModel(get()) }
    factory { BusViewModel(get()) }
}
