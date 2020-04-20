package com.mupper.gobus.utils

import android.Manifest
import android.content.Context
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Looper
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.location.LocationDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.data.source.resources.MapMarkerDataSource
import com.mupper.data.source.resources.TravelControlDataSource
import com.mupper.gobus.*
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.model.PermissionChecker
import com.nhaarman.mockitokotlin2.spy
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val app: GobusApp = getApplicationContext()

fun initMockedDi(vararg modules: Module) {
    stopKoin()
    startKoin {
        androidContext(app)
        modules(
            listOf(
                mockedAppModule,
                mockedDataSourceModule,
                repositoryModule,
                useCaseModule
            ) + modules
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

private val mockedDataSourceModule = module {
    single(named(DEPENDENCY_NAME_STATIC_USER_EMAIL)) { STATIC_USER_EMAIL }
    single { FakeMapResourcesDataSource().getBusIcon() }
    single<TravelControlDataSource<Drawable>> { FakeTravelControl() }
    single<LocationDataSource<LocationRequest, LocationCallback>> { spy(FakeLocationDataSource()) }
    single<BusLocalDataSource> { spy(FakeBusLocalDataSource()) }
    single<BusRemoteDataSource> { spy(FakeBusRemoteDataSource()) }
    single<TravelerLocalDataSource> { spy(FakeTravelerLocalDataSource()) }
    single<TravelerRemoteDataSource> { spy(FakeTravelerRemoteDataSource()) }
    single<MapMarkerDataSource<Marker, MarkerOptions>> { spy(FakeMapMarkerDataSource()) }
}
