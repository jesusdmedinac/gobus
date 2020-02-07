package com.mupper.gobus.data.source.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.commons.COLLECTION_BUS
import com.mupper.commons.FIELD_BUS_TRAVELERS
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.domain.traveler.Traveler
import com.mupper.gobus.data.toDomainBus
import com.mupper.gobus.data.toDomainBusWithTravelers
import com.mupper.gobus.data.toDomainTraveler
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BusFirebaseDataSource : BusRemoteDataSource {

    private val firestore = FirebaseFirestore.getInstance()

    private fun getBusDocument(path: String) = firestore.collection(COLLECTION_BUS).document(path)

    private fun getBusTravelersCollection(path: String) = getBusDocument(path).collection(
        FIELD_BUS_TRAVELERS
    )

    override fun addNewBusWithTravelers(bus: Bus, traveler: Traveler) {
        val busWithTravelers = bus.toDomainBusWithTravelers(traveler)
        val (path, _, _, travelers) = busWithTravelers
        getBusDocument(path).set(busWithTravelers.toDomainBus())
        travelers.forEach {
            with(it) {
                getBusTravelersCollection(path).document(email).set(this)
            }
        }
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
                            Traveler("", LatLng(0.0, 0.0), false)
                        }
                    }.toMutableList()
                    travalersList.removeAll { it.email.isEmpty() }

                    continuation.resume(travalersList)
                }
        }


    override suspend fun shareActualLocation(bus: BusWithTravelers, traveler: Traveler) {
        val (path) = bus
        val (email) = traveler

        getBusDocument(path).collection(FIELD_BUS_TRAVELERS).document(email).set(traveler)
    }
}