package com.mupper.core.database.traveler

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 *
 * The data access object for the [Traveler] class.
 *
 * @see Dao
 */
@Dao
interface TravelerDao {
    /**
     * Return all database added traveler ordering by email.
     *
     * @return LiveData<List<Traveler>>
     */
    @Query("SELECT * FROM traveler ORDER BY email")
    fun getAllTravelerLiveData(): LiveData<List<Traveler>>

    @Query("SELECT * from traveler LIMIT 1")
    fun getActualTraveler(): Traveler

    @Query("SELECT COUNT(email) FROM traveler")
    fun travelerCount(): Int

    /**
     * Add to database a list of travelers.
     *
     * @param travelers List<Traveler>
     */
    @Insert
    suspend fun insertTravelers(travelers: List<Traveler>)

    /**
     * Add to database a traveler.
     *
     * @param traveler Traveler
     */
    @Insert
    suspend fun insertTraveler(traveler: Traveler)
}