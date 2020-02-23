package com.mupper.gobus.data.source.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.gobus.commons.COLLECTION_BUS
import com.mupper.sharedtestcode.fakeBus
import com.mupper.sharedtestcode.fakeBusWithTravelers
import com.mupper.sharedtestcode.fakeTraveler
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BusFirebaseDataSourceTest {
    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockBusCollection: CollectionReference

    @Mock
    private lateinit var mockBusDocument: DocumentReference

    @Mock
    private lateinit var mockBusWithTravelersCollection: CollectionReference

    @Mock
    private lateinit var mockBusWithTravelersDocument: DocumentReference

    private lateinit var busFirebaseDataSource: BusFirebaseDataSource

    @Before
    fun setUp() {
        given(mockBusWithTravelersCollection.document(any())).willReturn(
            mockBusWithTravelersDocument
        )
        given(mockBusDocument.collection(any())).willReturn(mockBusWithTravelersCollection)
        given(mockBusCollection.document(any())).willReturn(mockBusDocument)
        given(mockFirestore.collection(COLLECTION_BUS)).willReturn(mockBusCollection)

        busFirebaseDataSource = BusFirebaseDataSource(mockFirestore)
    }

    @Test
    fun `addNewBusWithTravelers should call set of mockDocument`() {
        // GIVEN
        with(busFirebaseDataSource) {
            val bus = fakeBus.copy()
            val traveler = fakeTraveler.copy()

            // WHEN
            addNewBusWithTravelers(bus, traveler)

            // THEN
            verify(mockBusDocument).set(any())
        }
    }

    @Test
    fun `shareActualLocation should call set of mockDocument`() {
        runBlocking {
            // GIVEN
            with(busFirebaseDataSource) {
                val busWithTravelers = fakeBusWithTravelers.copy()
                val traveler = fakeTraveler.copy()

                // WHEN
                shareActualLocation(busWithTravelers, traveler)

                // THEN
                verify(mockBusWithTravelersDocument).set(any())
            }
        }
    }
}