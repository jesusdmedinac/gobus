package com.mupper.features

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.features.bus.GetActualBusWithTravelers
import com.mupper.sharedtestcode.mockedBusWithTravelers
import com.mupper.sharedtestcode.mockedLatLng
import com.mupper.sharedtestcode.mockedTraveler
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShareActualLocationTest {
    @Mock
    lateinit var getActualBusWithTravelers: GetActualBusWithTravelers

    @Mock
    lateinit var travelerLocalDataSource: TravelerLocalDataSource

    @Mock
    lateinit var travelerRemoteDataSource: TravelerRemoteDataSource

    @Mock
    lateinit var busLocalDataSource: BusLocalDataSource

    @Mock
    lateinit var busRemoteDataSource: BusRemoteDataSource

    lateinit var shareActualLocation: ShareActualLocation

    @Before
    fun setUp() {
        shareActualLocation = ShareActualLocation(
            getActualBusWithTravelers,
            travelerLocalDataSource,
            travelerRemoteDataSource,
            busLocalDataSource,
            busRemoteDataSource,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `invoke should call invoke of getActualBusWithTravelers with given LatLng`() {
        runBlocking {
            // GIVEN
            given(getActualBusWithTravelers.invoke()).willReturn(mockedBusWithTravelers.copy())

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verify(getActualBusWithTravelers).invoke()
        }
    }

    @Test
    fun `invoke should not call any of shareActualLocation when busWithTravelers does not have travelers`() {
        runBlocking {
            // GIVEN
            given(getActualBusWithTravelers.invoke()).willReturn(mockedBusWithTravelers.copy())

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verifyZeroInteractions(busLocalDataSource)
            verifyZeroInteractions(busRemoteDataSource)
            verifyZeroInteractions(travelerLocalDataSource)
            verifyZeroInteractions(travelerRemoteDataSource)
        }
    }

    @Test
    fun `invoke should call shareActualLocation of busLocalDataSource with found busWithTravelers when busWithTravelers has travelers`() {
        runBlocking {
            // GIVEN
            val expectedBusWithTravelersWithTravelers = mockedBusWithTravelers.copy(
                travelers = listOf(
                    mockedTraveler.copy("traveler 1"),
                    mockedTraveler.copy("traveler 2")
                )
            )
            given(getActualBusWithTravelers.invoke()).willReturn(
                expectedBusWithTravelersWithTravelers
            )

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verify(busLocalDataSource).shareActualLocation(expectedBusWithTravelersWithTravelers)
        }
    }

    @Test
    fun `invoke should call shareActualLocation of busRemoteDataSource with found busWithTravelers when busWithTravelers has travelers`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = mockedTraveler.copy("traveler 1")
            val expectedBusWithTravelersWithTravelers = mockedBusWithTravelers.copy(
                travelers = listOf(
                    expectedTraveler,
                    mockedTraveler.copy("traveler 2")
                )
            )
            given(getActualBusWithTravelers.invoke()).willReturn(
                expectedBusWithTravelersWithTravelers
            )

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verify(busRemoteDataSource).shareActualLocation(
                expectedBusWithTravelersWithTravelers,
                expectedTraveler
            )
        }
    }

    @Test
    fun `invoke should call shareActualLocation of travelerLocalDataSource with found busWithTravelers when busWithTravelers has travelers`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = mockedTraveler.copy("traveler 1")
            val expectedBusWithTravelersWithTravelers = mockedBusWithTravelers.copy(
                travelers = listOf(
                    expectedTraveler,
                    mockedTraveler.copy("traveler 2")
                )
            )
            given(getActualBusWithTravelers.invoke()).willReturn(
                expectedBusWithTravelersWithTravelers
            )

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verify(travelerLocalDataSource).shareActualLocation(expectedTraveler)
        }
    }

    @Test
    fun `invoke should call shareActualLocation of travelerRemoteDataSource with found busWithTravelers when busWithTravelers has travelers`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = mockedTraveler.copy("traveler 1")
            val expectedBusWithTravelersWithTravelers = mockedBusWithTravelers.copy(
                travelers = listOf(
                    expectedTraveler,
                    mockedTraveler.copy("traveler 2")
                )
            )
            given(getActualBusWithTravelers.invoke()).willReturn(
                expectedBusWithTravelersWithTravelers
            )

            // WHEN
            shareActualLocation.invoke(mockedLatLng.copy())

            // THEN
            verify(travelerRemoteDataSource).shareActualLocation(expectedTraveler)
        }
    }
}