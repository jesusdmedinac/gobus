package com.mupper.gobus.data.database.bus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.mupper.gobus.data.database.relations.BusWithTravelers

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
@Dao
interface BusDao {
    @Query("SELECT count(*) FROM bus WHERE path = :path")
    fun getCount(path: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBus(bus: Bus)

    @Update
    suspend fun updateBus(bus: Bus)

    @Transaction
    @Query("SELECT * FROM bus WHERE is_traveling = 1")
    fun getBusWithTravelers(): List<BusWithTravelers>
}