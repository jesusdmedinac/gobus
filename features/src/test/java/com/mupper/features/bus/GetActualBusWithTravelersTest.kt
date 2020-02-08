package com.mupper.features.bus

import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.sharedtestcode.mockedBusWithTravelers
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
    lateinit var busLocalDataSource: BusLocalDataSource

    @Mock
    lateinit var busRemoteDataSource: BusRemoteDataSource

    lateinit var getActualBusWithTravelers: GetActualBusWithTravelers

    @Before
    fun setUp() {
        getActualBusWithTravelers = GetActualBusWithTravelers(busLocalDataSource, busRemoteDataSource)
    }

    @Test
    fun `invoke should return null when getTravelingBusWithTravelers returns empty list`() {
        runBlocking {
            // GIVEN
            given(busLocalDataSource.getTravelingBusWithTravelers()).willReturn(emptyList())

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
            val expectedBusWithTravelers = mockedBusWithTravelers.copy("path 1")
            val busWithTravelers = listOf(
                expectedBusWithTravelers,
                mockedBusWithTravelers.copy("path 2")
            )
            given(busLocalDataSource.getTravelingBusWithTravelers()).willReturn(busWithTravelers)

            // WHEN
            val actualBusWithTravelers = getActualBusWithTravelers.invoke()

            // THEN
            assertThat(actualBusWithTravelers, `is`(expectedBusWithTravelers))
        }
    }
}