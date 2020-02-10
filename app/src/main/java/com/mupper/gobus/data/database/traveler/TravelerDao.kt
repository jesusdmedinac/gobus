package com.mupper.gobus.data.database.traveler

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 *
 * The data access object for the [Traveler] class.
 *
 * @see Dao
 */
@Dao
interface TravelerDao {
    @Query("SELECT * FROM traveler ORDER BY email")
    fun getAllTravelerLiveData(): LiveData<List<Traveler>>

    @Query("SELECT * from traveler WHERE email = :email")
    fun findTravelerByEmail(email: String): Traveler?

    @Query("UPDATE traveler SET latitude = :latitude, longitude = :longitude WHERE email = :email")
    fun updateTraveler(email: String, latitude: Double, longitude: Double)

    @Query("SELECT COUNT(email) FROM traveler")
    fun travelerCount(): Int

    @Insert
    suspend fun insertTravelers(travelers: List<Traveler>)

    @Insert
    suspend fun insertTraveler(traveler: Traveler)
}
