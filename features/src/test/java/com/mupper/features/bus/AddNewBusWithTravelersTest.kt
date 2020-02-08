package com.mupper.features.bus

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.sharedtestcode.mockedBus
import com.mupper.sharedtestcode.mockedTraveler
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddNewBusWithTravelersTest {

    @Mock
    lateinit var getActualTraveler: GetActualTraveler

    @Mock
    lateinit var busLocalDataSource: BusLocalDataSource

    @Mock
    lateinit var busRemoteDataSource: BusRemoteDataSource

    lateinit var addNewBusWithTravelers: AddNewBusWithTravelers

    @Before
    fun setUp() {
        addNewBusWithTravelers =
            AddNewBusWithTravelers(getActualTraveler, busLocalDataSource, busRemoteDataSource, Dispatchers.Unconfined)
    }

    @Test
    fun `invoke should call invoke of getActualTraveler with given Bus`() {
        runBlocking {
            // GIVEN
            val expectedPath = "bus path"
            val bus = mockedBus.copy(path = expectedPath)

            // WHEN
            addNewBusWithTravelers.invoke(bus)

            // THEN
            verify(getActualTraveler).invoke(expectedPath)
        }
    }

    @Test
    fun `invoke should call addNewBusWithTravelers of busRemoteDataSource with given Bus and Traveler`() {
        runBlocking {
            // GIVEN
            val expectedBus = mockedBus.copy()
            val expectedTraveler = mockedTraveler.copy()
            given(getActualTraveler.invoke(expectedBus.path)).willReturn(expectedTraveler)

            // WHEN
            addNewBusWithTravelers.invoke(expectedBus)

            // THEN
            verify(busRemoteDataSource).addNewBusWithTravelers(expectedBus, expectedTraveler)
        }
    }

    @Test
    fun `invoke should call addNewBus of busLocalDataSource with given Bus`() {
        runBlocking {
            // GIVEN
            val expectedBus = mockedBus.copy()
            val expectedTraveler = mockedTraveler.copy()
            given(getActualTraveler.invoke(expectedBus.path)).willReturn(expectedTraveler)

            // WHEN
            addNewBusWithTravelers.invoke(expectedBus)

            // THEN
            verify(busLocalDataSource).addNewBus(expectedBus)
        }
    }
}