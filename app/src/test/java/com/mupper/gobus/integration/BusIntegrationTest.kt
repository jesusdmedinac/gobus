@file:Suppress("DEPRECATION")

package com.mupper.gobus.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.gobus.*
import com.mupper.gobus.commons.Event
import com.mupper.gobus.utils.*
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.sharedtestcode.fakeTraveler
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyBlocking
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class BusIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var spyFakeTravelerLocalDataSource: FakeTravelerLocalDataSource

    private lateinit var spyFakeTravelerRemoteDataSource: FakeTravelerRemoteDataSource

    private lateinit var spyFakeBusLocalDataSource: FakeBusLocalDataSource

    private lateinit var spyFakeBusRemoteDataSource: FakeBusRemoteDataSource

    @Mock
    private lateinit var mockPathColorIntObserver: Observer<Int>

    @Mock
    private lateinit var mockPathColorObserver: Observer<String>

    @Mock
    private lateinit var mockIsTravelingStateToNewBusObserver: Observer<Boolean>

    @Mock
    lateinit var mockShowColorPickerDialogObserver: Observer<Event<Int?>>

    private lateinit var busViewModel: BusViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val vmModule = module {
            factory { BusViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)
        busViewModel = get()
        spyFakeTravelerLocalDataSource =
            get<TravelerLocalDataSource>() as FakeTravelerLocalDataSource
        spyFakeTravelerRemoteDataSource =
            get<TravelerRemoteDataSource>() as FakeTravelerRemoteDataSource
        spyFakeBusLocalDataSource = get<BusLocalDataSource>() as FakeBusLocalDataSource
        spyFakeBusRemoteDataSource = get<BusRemoteDataSource>() as FakeBusRemoteDataSource
    }

    @Test
    fun `showColorPickerDialog should set defaultColor to pathColorIntLiveData`() {
        with(busViewModel) {
            // GIVEN
            pathColorIntLiveData.observeForever(mockPathColorIntObserver)

            // WHEN
            val defaultColor = R.color.md_red_500
            showColorPickerDialog(defaultColor)

            // THEN
            verify(mockPathColorIntObserver).onChanged(defaultColor)
        }
    }

    @Test
    fun `showColorPickerDialog should not set defaultColor to pathColorIntLiveData if defaultColor was set previously`() {
        with(busViewModel) {
            // GIVEN
            pathColorIntLiveData.observeForever(mockPathColorIntObserver)
            val defaultColor = R.color.md_red_500
            showColorPickerDialog(defaultColor)

            // WHEN
            val anotherColor = R.color.md_amber_500
            showColorPickerDialog(anotherColor)

            // THEN
            verify(mockPathColorIntObserver, times(1)).onChanged(ArgumentMatchers.anyInt())
        }
    }

    @Test
    fun `showColorPickerDialog should call showColorPickerDialog event with defaultColor`() {
        with(busViewModel) {
            // GIVEN
            showColorPickerDialog.observeForever(mockShowColorPickerDialogObserver)

            // WHEN
            val expectedDefaultColor = R.color.md_red_500
            showColorPickerDialog(expectedDefaultColor)

            // THEN
            val expectedShowColorPickerDialogEvent = showColorPickerDialog.value
            verify(mockShowColorPickerDialogObserver).onChanged(expectedShowColorPickerDialogEvent)
            MatcherAssert.assertThat(
                expectedShowColorPickerDialogEvent?.peekContent(),
                CoreMatchers.`is`(expectedDefaultColor)
            )
        }
    }

    @Test
    fun `onColorPicked should set int color to pathColorIntLiveData`() {
        with(busViewModel) {
            // GIVEN
            pathColorIntLiveData.observeForever(mockPathColorIntObserver)

            // WHEN
            val expectedColor = R.color.md_amber_500
            onColorPicked(expectedColor, "")

            // THEN
            verify(mockPathColorIntObserver).onChanged(expectedColor)
        }
    }

    @Test
    fun `onColorPicked should set String color to pathColorLiveData`() {
        with(busViewModel) {
            // GIVEN
            pathColorLiveData.observeForever(mockPathColorObserver)

            // WHEN
            val expectedColorString = "#f44336"
            onColorPicked(0, expectedColorString)

            // THEN
            verify(mockPathColorObserver).onChanged(expectedColorString)
        }
    }

    @Test
    fun `saveNewBusToStartTravel should set isTravelingStatteToNewBusLiveData to true`() {
        with(busViewModel) {
            // GIVEN
            isTravelingStateToNewBus.observeForever(mockIsTravelingStateToNewBusObserver)

            // WHEN
            saveNewBusToStartTravel()

            // THEN
            verify(mockIsTravelingStateToNewBusObserver).onChanged(true)
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call findTravelerByEmail of travelerLocalDataSource with expected email`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTraveler = fakeTraveler.copy()
                spyFakeTravelerLocalDataSource.traveler = expectedTraveler

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verify(spyFakeTravelerLocalDataSource).findTravelerByEmail(expectedTraveler.email)
            }
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call getTravelerCount of travelerLocalDataSource given expected traveler`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTraveler = fakeTraveler.copy()
                spyFakeTravelerLocalDataSource.traveler = expectedTraveler

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verifyBlocking(spyFakeTravelerLocalDataSource) {
                    getTravelerCount()
                }
            }
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call findTravelerByEmail of travelerLocalDataSource given expected email`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTraveler = fakeTraveler.copy()
                spyFakeTravelerLocalDataSource.traveler = expectedTraveler

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verifyBlocking(spyFakeTravelerLocalDataSource) {
                    findTravelerByEmail(expectedTraveler.email)
                }
            }
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call findTravelerByEmail of travelerRemoteDataSource given expected email`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTravelerEmail = fakeTraveler.copy().email
                spyFakeTravelerLocalDataSource.travelerCount = 0

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verifyBlocking(spyFakeTravelerRemoteDataSource) {
                    findTravelerByEmail(expectedTravelerEmail)
                }
            }
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call addTraveler of travelerRemoteDataSource given expected traveler`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTraveler = fakeTraveler.copy()
                spyFakeTravelerLocalDataSource.travelerCount = 0
                spyFakeTravelerRemoteDataSource.traveler = null

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verifyBlocking(spyFakeTravelerRemoteDataSource) {
                    addTraveler(expectedTraveler)
                }
            }
        }
    }

    @Test
    fun `saveNewBusToStartTravel should call insertTraveler of travelerLocalDataSource with expected travelingPath and remoteTraveler`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTravelingPath = "path"
                pathNameLiveData.value = expectedTravelingPath
                val expectedTraveler = fakeTraveler.copy()
                spyFakeTravelerLocalDataSource.travelerCount = 0

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verifyBlocking(spyFakeTravelerLocalDataSource) {
                    insertTraveler(expectedTravelingPath, expectedTraveler)
                }
            }
        }
    }
}