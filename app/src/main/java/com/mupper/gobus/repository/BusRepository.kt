package com.mupper.gobus.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.core.utils.COLLECTION_BUS
import com.mupper.gobus.GobusApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.mupper.core.database.bus.Bus as DbBus


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class BusRepository(application: GobusApp) {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val db = application.db

    suspend fun addNewBus(bus: DbBus) = withContext(Dispatchers.IO) {
        db.busDao().insertBus(bus)

        if (db.busDao().getCount(bus.path) > 0) {
            firestore.collection(COLLECTION_BUS).document(bus.path).set(bus)
        }
    }
}

