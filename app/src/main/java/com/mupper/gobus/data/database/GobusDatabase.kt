package com.mupper.gobus.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mupper.gobus.commons.DATABASE_NAME
import com.mupper.gobus.data.database.bus.Bus
import com.mupper.gobus.data.database.bus.BusDao
import com.mupper.gobus.data.database.traveler.Traveler
import com.mupper.gobus.data.database.traveler.TravelerDao

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
@Database(
    entities = [Traveler::class, Bus::class],
    version = 1,
    exportSchema = false
)
abstract class GobusDatabase : RoomDatabase() {
    abstract fun travelerDao(): TravelerDao
    abstract fun busDao(): BusDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: GobusDatabase? = null

        fun getInstance(context: Context): GobusDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GobusDatabase {
            return Room.databaseBuilder(context, GobusDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }
}
