package com.mupper.gobus.repository

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.core.utils.*
import com.mupper.gobus.GobusApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import com.mupper.core.database.bus.Bus as DbBus


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class BusRepository(application: GobusApp, val travelerRepository: TravelerRepository) {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = application.db

    suspend fun addNewBus(bus: DbBus) = withContext(Dispatchers.IO) {
        db.busDao().insertBus(bus)

        if (db.busDao().getCount(bus.path) > 0) {
            firestore.collection(COLLECTION_BUS).document(bus.path).set(bus)
        }
    }

    private suspend fun getActualBusDocument() = withContext(Dispatchers.IO) {
        val busWithTravelers = db.busDao().getBusWithTravelers()

        firestore.collection(COLLECTION_BUS)
            .document(busWithTravelers[0].bus.path)
    }

    private suspend fun getActualTravelerEmail() =
        suspendCancellableCoroutine<String> { continuation ->
            travelerRepository.getActualTravelerDocument().get().addOnSuccessListener {
                continuation.resume(it[FIELD_TRAVELER_EMAIL].toString())
            }
        }

    suspend fun shareActualLocation(newLocation: LatLng) {
        val (latitude, longitude) = newLocation
        val actualBusDocument = getActualBusDocument()
        val actualTravelerEmail = getActualTravelerEmail()

        val latLngHashMap = hashMapOf(
            FIELD_TRAVELER_CURRENT_POSITION_LATITUDE to latitude,
            FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE to longitude
        )
        val currentPositionHashMap = hashMapOf(
            FIELD_TRAVELER_CURRENT_POSITION to latLngHashMap
        )
        actualBusDocument.collection(FIELD_BUS_TRAVELERS).document(actualTravelerEmail).set(
            currentPositionHashMap
        )
    }
}

