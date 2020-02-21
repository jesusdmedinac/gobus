package com.mupper.gobus.viewmodel

import com.google.android.gms.maps.model.LatLng
import com.mupper.features.ShareActualLocation
import com.mupper.sharedtestcode.fakeLatLng
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TravelerViewModelTest {

    @Mock
    private lateinit var mockShareActualLocation: ShareActualLocation

    private lateinit var travelerViewModel: TravelerViewModel

    @Before
    fun setUp() {
        travelerViewModel =
            TravelerViewModel(mockShareActualLocation, Dispatchers.Unconfined)
    }

    @Test
    fun `shareActualLocation should call invoke of shareActualLocation with expected newLocation`() {
        runBlocking {
            // GIVEN
            with(travelerViewModel) {
                // WHEN
                val expectedNewLocation = fakeLatLng.copy()
                shareActualLocation(expectedNewLocation)

                // THEN
                verify(mockShareActualLocation).invoke(expectedNewLocation)
            }
        }
    }
}
