package com.mupper.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mupper.core.database.traveler.Traveler
import com.mupper.core.database.traveler.TravelerDao
import com.mupper.core.utils.DATABASE_NAME

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
@Database(
    entities = [Traveler::class],
    version = 1
)
abstract class GobusDatabase : RoomDatabase() {
    abstract fun travelerDao(): TravelerDao

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