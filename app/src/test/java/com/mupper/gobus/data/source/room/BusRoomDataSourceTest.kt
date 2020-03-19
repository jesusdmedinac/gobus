package com.mupper.gobus.data.source.room

import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.database.bus.BusDao
import com.mupper.gobus.data.mapper.toRoomBus
import com.mupper.gobus.data.mapper.toRoomBusWithTravelers
import com.mupper.sharedtestcode.fakeBus
import com.mupper.sharedtestcode.fakeBusWithTravelers
import com.mupper.sharedtestcode.fakeTravelingPath
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyBlocking
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BusRoomDataSourceTest {

    @Mock
    private lateinit var db: GobusDatabase

    @Mock
    private lateinit var busDao: BusDao

    private lateinit var busRoomDataSource: BusRoomDataSource

    @Before
    fun setUp() {
        whenever(db.busDao()).thenReturn(busDao)
        busRoomDataSource = BusRoomDataSource(db)
    }

    @Test
    fun `getCount should return travelerCount value`() {
        runBlocking {
            //GIVEN
            with(busRoomDataSource) {
                val expectedCount = 2
                given(busDao.busCount(fakeTravelingPath)).willReturn(expectedCount)

                // WHEN
                val actualCount = getBusCount(fakeTravelingPath)

                // THEN
                assertThat(actualCount, `is`(expectedCount))
            }
        }
    }

    @Test
    fun `getCount should call travelerCount function`() {
        runBlocking {
            //GIVEN
            with(busRoomDataSource) {
                val expectedCount = 2
                given(busDao.busCount(fakeTravelingPath)).willReturn(expectedCount)

                // WHEN
                getBusCount(fakeTravelingPath)

                // THEN
                verify(busDao).busCount(fakeTravelingPath)
            }
        }
    }

    @Test
    fun `getTravelingBusWithTravelers should return expected BusWithTravelers`() {
        runBlocking {
            // GIVEN
            with(busRoomDataSource) {
                val expectedListBusWithTravelers = listOf(
                    fakeBusWithTravelers.copy()
                )
                given(busDao.getBusWithTravelers()).willReturn(expectedListBusWithTravelers.map { it.toRoomBusWithTravelers() })

                // WHEN
                val actualTravelingBusWithTravelers = getTravelingBusWithTravelers()

                // THEN
                assertThat(actualTravelingBusWithTravelers, `is`(expectedListBusWithTravelers))
            }
        }
    }

    @Test
    fun `getTravelingBusWithTravelers should call getBusWithTravelers`() {
        runBlocking {
            // GIVEN
            with(busRoomDataSource) {
                val expectedListBusWithTravelers = listOf(
                    fakeBusWithTravelers.copy()
                )
                given(busDao.getBusWithTravelers()).willReturn(expectedListBusWithTravelers.map { it.toRoomBusWithTravelers() })

                // WHEN
                getTravelingBusWithTravelers()

                // THEN
                verifyBlocking(busDao) {
                    getBusWithTravelers()
                }
            }
        }
    }

    @Test
    fun `addNewBus should call insertBus function`() {
        runBlocking {
            // GIVEN
            with(busRoomDataSource) {
                val expectedBus = fakeBus.copy()
                // WHEN
                addNewBus(expectedBus)

                // THEN
                verifyBlocking(busDao) {
                    insertBus(expectedBus.toRoomBus())
                }
            }
        }
    }

    @Test
    fun `shareActualLocation should call updateBus function`() {
        runBlocking {
            // GIVEN
            with(busRoomDataSource) {
                val expectedBusWithTravelers = fakeBusWithTravelers.copy()
                // WHEN
                shareActualLocation(expectedBusWithTravelers)

                // THEN
                verifyBlocking(busDao) {
                    updateBus(expectedBusWithTravelers.toRoomBus())
                }
            }
        }
    }
}