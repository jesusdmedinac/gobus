package com.mupper.gobus

import android.app.Application
import com.google.android.gms.maps.model.BitmapDescriptor
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.resources.MapResourcesDataSource
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.bus.GetActualBusWithTravelers
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.source.LocationDataSource
import com.mupper.gobus.data.source.firebase.BusFirebaseDataSource
import com.mupper.gobus.data.source.firebase.TravelerFirebaseDataSource
import com.mupper.gobus.data.source.room.BusRoomDataSource
import com.mupper.gobus.data.source.resources.MapBitmapDescriptorDataSource
import com.mupper.gobus.data.source.room.TravelerRoomDataSource
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.PlayServiceLocationDataSource
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
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

const val staticUserEmail = "dmc12345628@gmail.com"
const val staticUserEmailName = "staticUserEmail"
const val uiDispatcherDependencieName = "uiDispatcher"
const val ioDispatcherDependencieName = "ioDispatcher"

private val appModule = module {
    single { GobusDatabase.getInstance(get()) }
    factory<MapResourcesDataSource<BitmapDescriptor>> { MapBitmapDescriptorDataSource(get()) }
    single<CoroutineDispatcher>(named(uiDispatcherDependencieName)) { Dispatchers.Main }
    single(named(ioDispatcherDependencieName)) { Dispatchers.IO }
}

val dataModule = module {
    single(named(staticUserEmailName)) { staticUserEmail }
    factory { TravelControl(get()) }
    factory<LocationDataSource> { PlayServiceLocationDataSource(get()) }
    factory<BusLocalDataSource> {
        BusRoomDataSource(
            get()
        )
    }
    factory<BusRemoteDataSource> { BusFirebaseDataSource() }
    factory<TravelerLocalDataSource> {
        TravelerRoomDataSource(
            get()
        )
    }
    factory<TravelerRemoteDataSource> { TravelerFirebaseDataSource() }
}

private val featureModule = module {
    factory { GetActualTraveler(get(named(staticUserEmailName)), get(), get()) }
    factory { AddNewBusWithTravelers(get(), get(), get(), get(named(ioDispatcherDependencieName))) }
    factory { GetActualBusWithTravelers(get(), get()) }
    factory {
        ShareActualLocation(
            get(), get(), get(), get(), get(), get(
                named(
                    ioDispatcherDependencieName
                )
            )
        )
    }
}

private val viewModelModule = module {
    factory { MapsViewModel(get(), get(), get(named(uiDispatcherDependencieName))) }
    factory { TravelerViewModel(get(), get(named(uiDispatcherDependencieName))) }
    factory { TravelViewModel(get(), get(named(uiDispatcherDependencieName))) }
    factory { BusViewModel(get(), get(named(uiDispatcherDependencieName))) }
}
