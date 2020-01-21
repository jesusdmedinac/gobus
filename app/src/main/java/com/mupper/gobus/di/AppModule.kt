package com.mupper.gobus.di

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import com.google.android.gms.maps.model.BitmapDescriptor
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
        GobusDatabase.getInstance(app)

    @Provides
    fun busMarkerBitmapDescriptorProvider(app: Application): BitmapDescriptor = app.applicationContext.resources.getBitmapFromVector(
        R.drawable.ic_bus_marker,
        app.getCompatColor(R.color.primaryDarkColor)
    )
}
