package com.mupper.gobus.di

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import com.mupper.commons.DATABASE_NAME
import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.firestore.TravelerRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.getBitmapFromVector
import com.mupper.gobus.commons.extension.getCompatColor
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.source.bus.BusFirebaseDataSource
import com.mupper.gobus.data.source.bus.BusRoomDataSource
import com.mupper.gobus.data.source.traveler.TravelerFirebaseDataSource
import com.mupper.gobus.data.source.traveler.TravelerRoomDataSource
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application) =
        Room.databaseBuilder(app, GobusDatabase::class.java, DATABASE_NAME)
            .build()

    @Provides
    fun busLocalDataSourceProvider(db: GobusDatabase): BusLocalDataSource = BusRoomDataSource(db)

    @Provides
    fun busRemoteDataSourceProvider(): BusRemoteDataSource = BusFirebaseDataSource()

    @Provides
    fun travelerLocalDataSourceProvider(db: GobusDatabase): TravelerLocalDataSource =
        TravelerRoomDataSource(db)

    @Provides
    fun travelerRemoteDataSource(): TravelerRemoteDataSource = TravelerFirebaseDataSource()

    @Provides
    fun locationRepository(app: Application): LocationRepository = LocationRepository(app)

    @Provides
    fun busMarkerBitmapDescriptorProvider(app: Application) = app.applicationContext.resources.getBitmapFromVector(
        R.drawable.ic_bus_marker,
        app.getCompatColor(R.color.primaryDarkColor)
    )

    @Provides
    fun travelControlProvider(app: Application) = TravelControl(app)
}