package com.mupper.core.database.traveler

import androidx.lifecycle.LiveData


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelerRepository (private val travelerDao: TravelerDao) {

    /*
     * Return all database added traveler ordering by email.
     *
     * @return LiveData<List<Traveler>>
     */
    fun getAllTravelerLiveData(): LiveData<List<Traveler>> =
        travelerDao.getAllTravelerLiveData()

    fun getActualTraveler(): Traveler = travelerDao.getActualTraveler()

    /**
     * Add to database a list of travelers.
     *
     * @param travelers List<Traveler>
     */
    suspend fun insertTravelers(travelers: List<Traveler>) =
        travelerDao.insertTravelers(travelers)

    /**
     * Add to database a traveler.
     *
     * @param traveler Traveler
     */
    suspend fun insertTraveler(traveler: Traveler) =
        travelerDao.insertTraveler(traveler)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: TravelerRepository? = null

        fun getInstance(travelerDao: TravelerDao) =
            instance ?: synchronized(this) {
                instance ?: TravelerRepository(travelerDao).also { instance = it }
            }
    }
}