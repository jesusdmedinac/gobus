package com.mupper.data.repository

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.sharedtestcode.mockedBus
import com.mupper.sharedtestcode.mockedBusWithTravelers
import com.mupper.sharedtestcode.mockedTraveler
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BusRepositoryTest {
    @Mock
    lateinit var busLocalDataSource: BusLocalDataSource

    @Mock
    lateinit var busRemoteDataSource: BusRemoteDataSource

    lateinit var busRepository: BusRepository

    @Before
    fun setUp() {
        busRepository = BusRepositoryDerived(busLocalDataSource, busRemoteDataSource)
    }

    @Test
    fun `addNewBusWithTravelers should call addNewBus of busLocalDataSource and addNewBusWithTravelers of busRemoteDataSource with given bus and traveler`() {
        runBlocking {
            // GIVEN
            val expectedBus = mockedBus.copy()
            val expectedTraveler = mockedTraveler.copy()

            // WHEN
            busRepository.addNewBusWithTravelers(expectedBus, expectedTraveler)

            // THEN
            verify(busLocalDataSource).addNewBus(expectedBus)
            verify(busRemoteDataSource).addNewBusWithTravelers(expectedBus, expectedTraveler)
        }
    }

    @Test
    fun `getTravelingBusWithTravelers should call getTravelingBusWithTravelers of busLocalDataSource`() {
        runBlocking {
            // WHEN
            busRepository.getTravelingBusWithTravelers()

            // THEN
            verify(busLocalDataSource).getTravelingBusWithTravelers()
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of busLocalDataSource and busRemoteDataSource with given bus and traveler`() {
        runBlocking {
            // GIVEN
            val expectedBus = mockedBusWithTravelers.copy()
            val expectedTraveler = mockedTraveler.copy()

            // WHEN
            busRepository.shareActualLocation(expectedBus, expectedTraveler)

            // THEN
            verify(busLocalDataSource).shareActualLocation(expectedBus)
            verify(busRemoteDataSource).shareActualLocation(expectedBus, expectedTraveler)
        }
    }
}