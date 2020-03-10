package com.mupper.data.repository

import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.sharedtestcode.fakeTraveler
import com.mupper.sharedtestcode.fakeTravelingPath
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class TravelerRepositoryTest {
    @Mock
    lateinit var travelerLocalDataSource: TravelerLocalDataSource

    @Mock
    lateinit var travelerRemoteDataSource: TravelerRemoteDataSource

    lateinit var travelerRepository: TravelerRepository

    @Before
    fun setUp() {
        travelerRepository = TravelerRepositoryDerived(
            fakeTraveler.copy().email,
            travelerLocalDataSource,
            travelerRemoteDataSource
        )
    }

    @Test
    fun `retrieveActualTraveler should return a Traveler when there is almost one on travelerRepository with given actualEmail`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getTravelerCount()).willReturn(1)
            val expectedTraveler = fakeTraveler.copy()
            given(travelerLocalDataSource.findTravelerByEmail(expectedTraveler.email)).willReturn(
                expectedTraveler
            )

            // WHEN
            val actualTraveler = travelerRepository.retrieveActualTraveler(fakeTravelingPath)

            // THEN
            MatcherAssert.assertThat(actualTraveler, CoreMatchers.`is`(expectedTraveler))
        }
    }

    @Test
    fun `retrieveActualTraveler should call findTravelerByEmail of travelerLocalDataSource with given email when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getTravelerCount()).willReturn(0)

            // WHEN
            travelerRepository.retrieveActualTraveler(fakeTravelingPath)

            // THEN
            verify(travelerLocalDataSource).findTravelerByEmail(fakeTraveler.copy().email)
        }
    }

    @Test
    fun `retrieveActualTraveler should call findTravelerByEmail of travelerRemoteDataSource with given email when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getTravelerCount()).willReturn(0)

            // WHEN
            travelerRepository.retrieveActualTraveler(fakeTravelingPath)

            // THEN
            verify(travelerRemoteDataSource).findTravelerByEmail(fakeTraveler.copy().email)
        }
    }

    @Test
    fun `retrieveActualTraveler should call invertTraveler of travelerLocalDataSource with expected travelingPath and remoteTraveler when getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getTravelerCount()).willReturn(0)
            val expectedTraveler = fakeTraveler.copy()
            given(travelerRemoteDataSource.findTravelerByEmail(expectedTraveler.email)).willReturn(
                expectedTraveler
            )

            // WHEN
            val expectedTravelingPath = fakeTravelingPath
            travelerRepository.retrieveActualTraveler(expectedTravelingPath)

            // THEN
            verify(travelerLocalDataSource).insertTraveler(expectedTravelingPath, expectedTraveler)
        }
    }

    @Test
    fun `retrieveActualTraveler should call addStaticTraveler when findTravelerByEmail of travelerRemoteDataSource returns null and getCount return 0`() {
        runBlocking {
            // GIVEN
            given(travelerLocalDataSource.getTravelerCount()).willReturn(0)

            // WHEN
            whenever(travelerRemoteDataSource.findTravelerByEmail(fakeTraveler.copy().email)).thenReturn(
                null
            )
            travelerRepository.retrieveActualTraveler(fakeTravelingPath)

            // THEN
            verify(travelerRemoteDataSource).addTraveler(any())
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of travelerLocalDataSource and travelerRemoteDataSource with given traveler`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()

            // WHEN
            travelerRepository.shareActualLocation(expectedTraveler)

            // THEN
            verify(travelerLocalDataSource).shareActualLocation(expectedTraveler)
            verify(travelerRemoteDataSource).shareActualLocation(expectedTraveler)
        }
    }
}