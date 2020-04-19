package com.mupper.gobus.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.domain.LatLng
import com.mupper.gobus.DEPENDENCY_NAME_UI_DISPATCHER
import com.mupper.gobus.data.mapper.toDomainBus
import com.mupper.gobus.utils.FakeBusLocalDataSource
import com.mupper.gobus.utils.FakeBusRemoteDataSource
import com.mupper.gobus.utils.FakeTravelerLocalDataSource
import com.mupper.gobus.utils.FakeTravelerRemoteDataSource
import com.mupper.gobus.utils.initMockedDi
import com.mupper.gobus.viewmodel.TravelerViewModel
import com.mupper.sharedtestcode.fakeBusWithTravelers
import com.mupper.sharedtestcode.fakeLatLng
import com.mupper.sharedtestcode.fakeTraveler
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TravelerIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var spyFakeBusLocalDataSource: FakeBusLocalDataSource

    private lateinit var spyFakeBusRemoteDataSource: FakeBusRemoteDataSource

    private lateinit var spyFakeTravelerLocalDataSource: FakeTravelerLocalDataSource

    private lateinit var spyFakeTravelerRemoteDataSource: FakeTravelerRemoteDataSource

    private lateinit var travelerViewModel: TravelerViewModel

    private lateinit var expectedNewLocation: LatLng

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val vmModel = module {
            factory { TravelerViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModel)
        travelerViewModel = get()
        spyFakeBusLocalDataSource = get<BusLocalDataSource>() as FakeBusLocalDataSource
        spyFakeBusRemoteDataSource = get<BusRemoteDataSource>() as FakeBusRemoteDataSource
        spyFakeTravelerLocalDataSource =
            get<TravelerLocalDataSource>() as FakeTravelerLocalDataSource
        spyFakeTravelerRemoteDataSource =
            get<TravelerRemoteDataSource>() as FakeTravelerRemoteDataSource
        expectedNewLocation = fakeLatLng.copy()
    }

    @Test
    fun `shareActualLocation should call getTravelingBusWithTravelers of busLocalDataSource`() {
        runBlocking {
            // GIVEN
            with(travelerViewModel) {
                // WHEN
                shareActualLocation(expectedNewLocation)

                // THEN
                verifyBlocking(spyFakeBusLocalDataSource) {
                    getTravelingBusWithTravelers()
                }
            }
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of busLocalDataSource with expected travelingBus`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()
            val expectedBusWithTravelingBus =
                fakeBusWithTravelers.copy(travelers = listOf(expectedTraveler))
            spyFakeBusLocalDataSource.travelingBusWithTravelersList =
                listOf(expectedBusWithTravelingBus)

            // WHEN
            travelerViewModel.shareActualLocation(expectedNewLocation)

            // THEN
            verifyBlocking(spyFakeBusLocalDataSource) {
                shareActualLocation(expectedBusWithTravelingBus)
            }
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of busRemoteDataSource with expected busWithWithTravelers and traveler`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()
            val expectedBusWithTravelingBus =
                fakeBusWithTravelers.copy(travelers = listOf(expectedTraveler))
            spyFakeBusLocalDataSource.travelingBusWithTravelersList =
                listOf(expectedBusWithTravelingBus)
            spyFakeBusRemoteDataSource.bus = expectedBusWithTravelingBus.toDomainBus()

            // WHEN
            travelerViewModel.shareActualLocation(expectedNewLocation)

            // THEN
            verifyBlocking(spyFakeBusRemoteDataSource) {
                shareActualLocation(expectedBusWithTravelingBus, expectedTraveler)
            }
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of travelerLocalDataSource with expected travelerInBus`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()
            val expectedBusWithTravelingBus =
                fakeBusWithTravelers.copy(travelers = listOf(expectedTraveler))
            spyFakeBusLocalDataSource.travelingBusWithTravelersList =
                listOf(expectedBusWithTravelingBus)

            // WHEN
            travelerViewModel.shareActualLocation(expectedNewLocation)

            // THEN
            verifyBlocking(spyFakeTravelerLocalDataSource) {
                shareActualLocation(expectedTraveler)
            }
        }
    }

    @Test
    fun `shareActualLocation should call shareActualLocation of travelerRemoteDataSource with expected travelerInBus`() {
        runBlocking {
            // GIVEN
            val expectedTraveler = fakeTraveler.copy()
            val expectedBusWithTravelingBus =
                fakeBusWithTravelers.copy(travelers = listOf(expectedTraveler))
            spyFakeBusLocalDataSource.travelingBusWithTravelersList =
                listOf(expectedBusWithTravelingBus)

            // WHEN
            travelerViewModel.shareActualLocation(expectedNewLocation)

            // THEN
            verifyBlocking(spyFakeTravelerRemoteDataSource) {
                shareActualLocation(expectedTraveler)
            }
        }
    }
}
