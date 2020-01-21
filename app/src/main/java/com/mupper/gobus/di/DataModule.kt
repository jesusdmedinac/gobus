package com.mupper.gobus.di

import android.app.Application
import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.data.source.firestore.TravelerRemoteDataSource
import com.mupper.data.source.room.BusLocalDataSource
import com.mupper.data.source.room.TravelerLocalDataSource
import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.source.bus.BusFirebaseDataSource
import com.mupper.gobus.data.source.bus.BusRoomDataSource
import com.mupper.gobus.data.source.traveler.TravelerFirebaseDataSource
import com.mupper.gobus.data.source.traveler.TravelerRoomDataSource
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun travelControlProvider(app: Application) = TravelControl(app)

    @Provides
    fun locationRepository(app: Application): LocationRepository = LocationRepository(app)

    @Provides
    fun busLocalDataSourceProvider(db: GobusDatabase): BusLocalDataSource = BusRoomDataSource(db)

    @Provides
    fun busRemoteDataSourceProvider(): BusRemoteDataSource = BusFirebaseDataSource()

    @Provides
    fun travelerLocalDataSourceProvider(db: GobusDatabase): TravelerLocalDataSource =
        TravelerRoomDataSource(db)

    @Provides
    fun travelerRemoteDataSource(): TravelerRemoteDataSource = TravelerFirebaseDataSource()
}