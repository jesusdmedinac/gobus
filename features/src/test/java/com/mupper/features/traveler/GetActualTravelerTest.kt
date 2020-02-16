package com.mupper.features.traveler

import com.mupper.data.repository.TravelerRepository
import com.mupper.sharedtestcode.fakeTraveler
import com.mupper.sharedtestcode.fakeTravelingPath
import com.nhaarman.mockitokotlin2.given
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
    lateinit var travelerRepository: TravelerRepository

    lateinit var getActualTraveler: GetActualTraveler

    @Before
    fun setUp() {
        getActualTraveler = GetActualTraveler(travelerRepository)
    }

    @Test
    fun `invoke should return expected traveler from retrieveActualTraveler`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()
            given(travelerRepository.retrieveActualTraveler(fakeTravelingPath)).willReturn(
                expectedTraveler
            )

            // WHEN
            val actualTraveler = getActualTraveler.invoke(fakeTravelingPath)

            // THEN
            assertThat(actualTraveler, `is`(expectedTraveler))
        }
    }
}