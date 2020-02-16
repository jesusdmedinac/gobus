package com.mupper.features.bus

import com.mupper.data.repository.BusRepository
import com.mupper.sharedtestcode.fakeBusWithTravelers
import com.nhaarman.mockitokotlin2.given
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetActualBusWithTravelersTest {
    @Mock
    lateinit var busRepository: BusRepository

    lateinit var getActualBusWithTravelers: GetActualBusWithTravelers

    @Before
    fun setUp() {
        getActualBusWithTravelers = GetActualBusWithTravelers(busRepository)
    }

    @Test
    fun `invoke should return null when getTravelingBusWithTravelers returns empty list`() {
        runBlocking {
            // GIVEN
            given(busRepository.getTravelingBusWithTravelers()).willReturn(emptyList())

            // WHEN
            val busWithTravelers = getActualBusWithTravelers.invoke()

            // THEN
            assertThat(busWithTravelers, `is`(nullValue()))
        }
    }

    @Test
    fun `invoke should return first busWithTravelers when getTravelingBusWithTravelers returns a list of BusWithTravelers`() {
        runBlocking {
            // GIVEN
            val expectedBusWithTravelers = fakeBusWithTravelers.copy("path 1")
            val busWithTravelers = listOf(
                expectedBusWithTravelers,
                fakeBusWithTravelers.copy("path 2")
            )
            given(busRepository.getTravelingBusWithTravelers()).willReturn(busWithTravelers)

            // WHEN
            val actualBusWithTravelers = getActualBusWithTravelers.invoke()

            // THEN
            assertThat(actualBusWithTravelers, `is`(expectedBusWithTravelers))
        }
    }
}