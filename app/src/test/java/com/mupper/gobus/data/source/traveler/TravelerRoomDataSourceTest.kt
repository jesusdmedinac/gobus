package com.mupper.gobus.data.source.traveler

import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.database.traveler.TravelerDao
import com.mupper.gobus.data.source.room.TravelerRoomDataSource
import com.mupper.gobus.data.mapper.toRoomTraveler
import com.mupper.sharedtestcode.fakeTraveler
import com.mupper.sharedtestcode.fakeTravelingPath
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TravelerRoomDataSourceTest {

    @Mock
    private lateinit var db: GobusDatabase

    @Mock
    private lateinit var travelerDao: TravelerDao

    private lateinit var travelerRoomDataSource: TravelerRoomDataSource

    @Before
    fun setUp() {
        whenever(db.travelerDao()).thenReturn(travelerDao)
        travelerRoomDataSource =
            TravelerRoomDataSource(db)
    }

    @Test
    fun `verify that getCount call travelerCount`() {
        runBlocking {
            travelerRoomDataSource.getCount()

            verify(travelerDao).travelerCount()
        }
    }

    @Test
    fun `verify that insertTraveler call insertTraveler`() {
        runBlocking {
            travelerRoomDataSource.insertTraveler(fakeTravelingPath, fakeTraveler)

            verify(travelerDao).insertTraveler(fakeTraveler.toRoomTraveler(fakeTravelingPath))
        }
    }

    @Test
    fun `verify that findTravelerByEmail call findTravelerByEmail`() {
        runBlocking {
            travelerRoomDataSource.findTravelerByEmail(fakeTraveler.email)

            verify(travelerDao).findTravelerByEmail(fakeTraveler.email)
        }
    }

    @Test
    fun `verify that shareActualLocation call updateTraveler`() {
        runBlocking {
            travelerRoomDataSource.shareActualLocation(fakeTraveler)

            with (fakeTraveler) {
                val (latitude, longitude) = currentPosition
                verify(travelerDao).updateTraveler(email, latitude, longitude)
            }
        }
    }

    @After
    fun tearDown() {
    }
}