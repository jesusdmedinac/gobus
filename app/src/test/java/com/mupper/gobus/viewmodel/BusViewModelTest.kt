package com.mupper.gobus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.gobus.R
import com.mupper.gobus.commons.Event
import com.mupper.sharedtestcode.fakeBus
import com.mupper.sharedtestcode.fakeTraveler
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BusViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockAddNewBusWithTravelers: AddNewBusWithTravelers

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
        busViewModel = BusViewModel(mockAddNewBusWithTravelers, Dispatchers.Unconfined)
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
            verify(mockPathColorIntObserver, times(1)).onChanged(anyInt())
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
            assertThat(
                expectedShowColorPickerDialogEvent?.peekContent(),
                `is`(expectedDefaultColor)
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
    fun `saveNewBusToStartLevel should call invoke of addNewBusWithTravelers with expected DomainBus`() {
        runBlocking {
            with(busViewModel) {
                // GIVEN
                val expectedTraveler = fakeTraveler.copy()
                val expectedBus = fakeBus.copy(isTraveling = true)
                val (pathName, color, capacity) = expectedBus
                willReturn(expectedTraveler).given(mockAddNewBusWithTravelers).invoke(expectedBus)
                pathNameLiveData.value = pathName
                pathColorLiveData.value = color
                capacityLiveData.value = capacity.toString()

                // WHEN
                saveNewBusToStartTravel()

                // THEN
                verify(mockAddNewBusWithTravelers).invoke(expectedBus)
            }
        }
    }
}