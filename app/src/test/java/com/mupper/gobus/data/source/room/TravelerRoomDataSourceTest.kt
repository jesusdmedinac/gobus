package com.mupper.gobus.data.source.room

import com.mupper.gobus.data.database.GobusDatabase
import com.mupper.gobus.data.database.traveler.TravelerDao
import com.mupper.gobus.data.mapper.toDomainTraveler
import com.mupper.gobus.data.mapper.toRoomTraveler
import com.mupper.sharedtestcode.fakeTraveler
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
class TravelerRoomDataSourceTest {

    @Mock
    private lateinit var db: GobusDatabase

    @Mock
    private lateinit var travelerDao: TravelerDao

    private lateinit var travelerRoomDataSource: TravelerRoomDataSource

    @Before
    fun setUp() {
        whenever(db.travelerDao()).thenReturn(travelerDao)
        travelerRoomDataSource = TravelerRoomDataSource(db)
    }

    @Test
    fun `getCount should return travelerCount value`() {
        runBlocking {
            //GIVEN
            with(travelerRoomDataSource) {
                val expectedCount = 2
                given(travelerDao.travelerCount()).willReturn(expectedCount)

                // WHEN
                val actualCount = getTravelerCount()

                // THEN
                assertThat(actualCount, `is`(expectedCount))
            }
        }
    }

    @Test
    fun `getCount should call travelerCount function`() {
        runBlocking {
            //GIVEN
            with(travelerRoomDataSource) {
                val expectedCount = 2
                given(travelerDao.travelerCount())
                    .willReturn(expectedCount)

                // WHEN
                getTravelerCount()

                // THEN
                verifyBlocking(travelerDao) {
                    travelerCount()
                }
            }
        }
    }

    @Test
    fun `insertTraveler should call insertTraveler with given RoomTraveler`() {
        runBlocking {
            // GIVEN
            with(travelerRoomDataSource) {
                val expectedTraveler = fakeTraveler.copy()

                // WHEN
                insertTraveler(fakeTravelingPath, expectedTraveler)

                // THEN
                verifyBlocking(travelerDao) {
                    insertTraveler(expectedTraveler.toRoomTraveler(fakeTravelingPath))
                }
            }
        }
    }

    @Test
    fun `findTravelerByEmail should return a expected DomainTraveler`() {
        runBlocking {
            //GIVEN
            with(travelerRoomDataSource) {
                val expectedDomainTraveler = fakeTraveler.copy().toRoomTraveler(fakeTravelingPath)
                val (email) = expectedDomainTraveler
                given(travelerDao.findTravelerByEmail(email)).willReturn(expectedDomainTraveler)

                // WHEN
                val actualTravelerByEmail = findTravelerByEmail(email)

                // THEN
                assertThat(actualTravelerByEmail, `is`(expectedDomainTraveler.toDomainTraveler()))
            }
        }
    }

    @Test
    fun `findTravelerByEmail should call findTravelerByEmail function`() {
        runBlocking {
            //GIVEN
            with(travelerRoomDataSource) {
                val expectedDomainTraveler = fakeTraveler.copy().toRoomTraveler(fakeTravelingPath)
                val (email) = expectedDomainTraveler
                given(travelerDao.findTravelerByEmail(email)).willReturn(expectedDomainTraveler)

                // WHEN
                findTravelerByEmail(email)

                // THEN
                verifyBlocking(travelerDao) {
                    findTravelerByEmail(email)
                }
            }
        }
    }

    @Test
    fun `shareActualLocation should call updateTraveler with any arguments`() {
        runBlocking {
            // GIVEN
            with(travelerRoomDataSource) {
                // WHEN
                val expectedTraveler = fakeTraveler.copy()
                val (email, currentPosition) = expectedTraveler
                val (latitude, longitude) = currentPosition
                shareActualLocation(expectedTraveler)

                // THEN
                verifyBlocking(travelerDao) {
                    updateTraveler(email, latitude, longitude)
                }
            }
        }
    }
}