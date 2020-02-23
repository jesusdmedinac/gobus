package com.mupper.gobus.data.source.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mupper.gobus.commons.COLLECTION_TRAVELER
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
class TravelerFirebaseDataSourceTest {
    @Mock
    private lateinit var mockFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockTravelerCollection: CollectionReference

    @Mock
    private lateinit var mockTravelerDocument: DocumentReference

    private lateinit var travelerFirebaseDataSource: TravelerFirebaseDataSource

    @Before
    fun setUp() {
        given(mockTravelerCollection.document(any())).willReturn(mockTravelerDocument)
        given(mockFirestore.collection(COLLECTION_TRAVELER)).willReturn(mockTravelerCollection)

        travelerFirebaseDataSource = TravelerFirebaseDataSource(mockFirestore)
    }

    @Test
    fun `shareActualLocation should call set of mockDocument`() {
        runBlocking {
            // GIVEN
            with(travelerFirebaseDataSource) {
                val traveler = fakeTraveler.copy()

                // WHEN
                shareActualLocation(traveler)

                // THEN
                verify(mockTravelerDocument).set(any())
            }
        }
    }
}