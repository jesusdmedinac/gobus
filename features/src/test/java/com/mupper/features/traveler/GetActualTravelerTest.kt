package com.mupper.features.traveler

import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.domain.traveler.Traveler
import com.mupper.sharedtestcode.mockedTraveler
import com.mupper.sharedtestcode.mockedTravelingPath
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetActualTravelerTest {
    @Mock
    lateinit var travelerLocalDataSource: TravelerLocalDataSource

    @Mock
    lateinit var travelerRemoteDataSource: TravelerRemoteDataSource

    lateinit var getActualTraveler: GetActualTraveler

    @Before
    fun setUp() {
        getActualTraveler =
            GetActualTraveler(
                mockedTraveler.copy().email,
                travelerLocalDataSource,
                travelerRemoteDataSource
            )
    }

    @Test
    fun `invoke should return a Traveler when there is almost one on travelerLocalDataSource with given actualEmail`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getCount()).willReturn(1)
            val expectedTraveler = mockedTraveler.copy()
            given(travelerLocalDataSource.findTravelerByEmail(expectedTraveler.email)).willReturn(
                expectedTraveler
            )

            // WHEN
            val actualTraveler = getActualTraveler.invoke(mockedTravelingPath)

            // THEN
            assertThat(actualTraveler, `is`(expectedTraveler))
        }
    }

    @Test
    fun `invoke should call findTravelerByEmail of travelerLocalDataSource with given email when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getCount()).willReturn(0)

            // WHEN
            getActualTraveler.invoke(mockedTravelingPath)

            // THEN
            verify(travelerLocalDataSource).findTravelerByEmail(mockedTraveler.copy().email)
        }
    }

    @Test
    fun `invoke should call findTravelerByEmail of travelerRemoteDataSource with given email when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getCount()).willReturn(0)

            // WHEN
            getActualTraveler.invoke(mockedTravelingPath)

            // THEN
            verify(travelerRemoteDataSource).findTravelerByEmail(mockedTraveler.copy().email)
        }
    }

    @Test
    fun `invoke should call invertTraveler of travelerLocalDataSource with expected travelingPath and remoteTraveler when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getCount()).willReturn(0)
            val expectedTraveler = mockedTraveler.copy()
            given(travelerRemoteDataSource.findTravelerByEmail(expectedTraveler.email)).willReturn(
                expectedTraveler
            )

            // WHEN
            val expectedTravelingPath = mockedTravelingPath
            getActualTraveler.invoke(expectedTravelingPath)

            // THEN
            verify(travelerLocalDataSource).insertTraveler(expectedTravelingPath, expectedTraveler)
        }
    }

    @Test
    fun `invoke should call addStaticTraveler when findTravelerByEmail of travelerRemoteDataSource returns null and getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getCount()).willReturn(0)

            // WHEN
            whenever(travelerRemoteDataSource.findTravelerByEmail(mockedTraveler.copy().email)).thenReturn(
                null
            )
            getActualTraveler.invoke(mockedTravelingPath)

            // THEN
            verify(travelerRemoteDataSource).addTraveler(any())
        }
    }
}