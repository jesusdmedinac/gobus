package com.mupper.gobus

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.model.PermissionChecker
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
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
    single { GobusDatabase.getInstance(get()) }
    single(named(DEPENDENCY_NAME_UI_DISPATCHER)) { Dispatchers.Unconfined }
    single { LocationServices.getFusedLocationProviderClient(app) }
    single { PermissionChecker(get(), Manifest.permission.ACCESS_FINE_LOCATION) }
    single { app.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    single { Looper.getMainLooper() }
    single { FirebaseFirestore.getInstance() }
    single(named(DEPENDENCY_NAME_IO_DISPATCHER)) { Dispatchers.Unconfined }
}

private val featureModule = module {
    factory { GetActualTraveler(get()) }
    factory { AddNewBusWithTravelers(get(), get(), get()) }
}

private val mockedViewModule: Module = module {
    factory { MapViewModel(get(), get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
    factory { TravelerViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
    factory { TravelViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
}