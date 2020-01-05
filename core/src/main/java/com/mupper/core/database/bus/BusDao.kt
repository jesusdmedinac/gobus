package com.mupper.core.database.bus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
@Dao
interface BusDao {
    @Query("SELECT count(*) FROM bus WHERE path = :path")
    fun getCount(path: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBus(bus: Bus)
}