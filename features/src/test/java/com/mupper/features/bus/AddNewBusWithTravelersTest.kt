package com.mupper.features.bus

import com.mupper.data.repository.BusRepository
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.sharedtestcode.fakeBus
import com.mupper.sharedtestcode.fakeTraveler
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
    lateinit var busRepository: BusRepository

    lateinit var addNewBusWithTravelers: AddNewBusWithTravelers

    @Before
    fun setUp() {
        addNewBusWithTravelers =
            AddNewBusWithTravelers(getActualTraveler, busRepository, Dispatchers.Unconfined)
    }

    @Test
    fun `invoke should call invoke of getActualTraveler with given Bus`() {
        runBlocking {
            // GIVEN
            val expectedPath = "bus path"
            val bus = fakeBus.copy(path = expectedPath)

            // WHEN
            addNewBusWithTravelers.invoke(bus)

            // THEN
            verify(getActualTraveler).invoke(expectedPath)
        }
    }

    @Test
    fun `invoke should call addNewBusWithTravelers of busRepository with given Bus and Traveler`() {
        runBlocking {
            // GIVEN
            val expectedBus = fakeBus.copy()
            val expectedTraveler = fakeTraveler.copy()
            given(getActualTraveler.invoke(expectedBus.path)).willReturn(expectedTraveler)

            // WHEN
            addNewBusWithTravelers.invoke(expectedBus)

            // THEN
            verify(busRepository).addNewBusWithTravelers(expectedBus, expectedTraveler)
        }
    }
}