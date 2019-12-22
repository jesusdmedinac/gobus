package com.mupper.gobus.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.core.utils.*
import com.mupper.gobus.GobusApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import com.mupper.core.database.traveler.CurrentPosition as DbCurrentPosition
import com.mupper.core.database.traveler.Traveler as DbTraveler
import com.mupper.core.firestore.traveler.CurrentPosition as FirestoreCurrentPosition
import com.mupper.core.firestore.traveler.Traveler as FirestoreTraveler

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelerRepository(application: GobusApp) {
    private val actualEmail = "dmc12345628@gmail.com"

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = application.db

    suspend fun getActualTraveler(): DbTraveler? = withContext(Dispatchers.IO) {
        with(db.travelerDao()) {
            if (travelerCount() <= 0) {
                val firestoreTraveler = getActualFirestoreTraveler()

                insertTraveler(firestoreTraveler.convertToDbTraveler())
            }

            getActualTraveler()
        }
    }

    fun getActualTravelerDocument() = firestore.collection(COLLECTION_TRAVELER)
        .document(actualEmail)

    private suspend fun getActualFirestoreTraveler() =
        suspendCancellableCoroutine<FirestoreTraveler> { continuation ->
            getActualTravelerDocument().get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        continuation.resume(mapFirestorTraveler(it.result?.data))
                    }
                }
        }

    private fun mapFirestorTraveler(data: Map<String, Any>?): FirestoreTraveler {
        return with(data) {
            val currentPositionMap =
                this?.get(FIELD_TRAVELER_CURRENT_POSITION) as HashMap<String, Double>

            val currentPosition =
                FirestoreCurrentPosition(
                    currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LATITUDE] ?: 0.0,
                    currentPositionMap[FIELD_TRAVELER_CURRENT_POSITION_LONGITUDE] ?: 0.0
                )
            val email = this.get(FIELD_TRAVELER_EMAIL) as String

            FirestoreTraveler(
                currentPosition,
                email
            )
        }
    }

    fun shareActualLocation(newLocation: LatLng) {
        getActualTravelerDocument().update(
            FIELD_TRAVELER_CURRENT_POSITION, FirestoreCurrentPosition(
                newLocation.latitude,
                newLocation.longitude
            )
        )
    }
}

private fun FirestoreTraveler.convertToDbTraveler() = DbTraveler(
    email,
    DbCurrentPosition(currentPosition.latitude, currentPosition.longitude)
)