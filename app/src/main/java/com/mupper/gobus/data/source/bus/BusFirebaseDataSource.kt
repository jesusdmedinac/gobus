package com.mupper.gobus.data.source.bus

import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.commons.COLLECTION_BUS
import com.mupper.commons.FIELD_BUS_TRAVELERS
import com.mupper.data.source.firestore.BusRemoteDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.bus.Bus
import com.mupper.domain.traveler.Traveler
import com.mupper.gobus.data.toDomainBus
import com.mupper.gobus.data.toDomainTraveler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BusFirebaseDataSource : BusRemoteDataSource {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getBusDocument(path: String) = firestore.collection(COLLECTION_BUS).document(path)

    private fun getBusTravelersCollection(path: String) = getBusDocument(path).collection(
        FIELD_BUS_TRAVELERS
    )

    override fun addNewBus(bus: Bus) {
        getBusDocument(bus.path).set(bus)
    }

    override suspend fun findBusByPathName(path: String): Bus =
        suspendCancellableCoroutine { continuation ->
            getBusDocument(path).get().addOnSuccessListener { document ->
                document.data?.let {
                    continuation.resume(it.toDomainBus())
                }
            }
        }

    override suspend fun findBusTravelersByPathName(path: String): List<Traveler> =
        suspendCancellableCoroutine { continuation ->
            getBusTravelersCollection(path).get()
                .addOnSuccessListener { document ->
                    val travalersList: MutableList<Traveler> = document.documents.map {
                        if (it.data != null) {
                            it.data!!.toDomainTraveler()
                        } else {
                            Traveler("", LatLng(0f, 0f), false)
                        }
                    }.toMutableList()
                    travalersList.removeAll { it.email.isEmpty() }

                    continuation.resume(travalersList)
                }
        }


    override suspend fun shareActualLocation(bus: Bus, traveler: Traveler) {
        val (path) = bus
        val (email) = traveler

        getBusDocument(path).collection(FIELD_BUS_TRAVELERS).document(email).set(traveler)
    }
}