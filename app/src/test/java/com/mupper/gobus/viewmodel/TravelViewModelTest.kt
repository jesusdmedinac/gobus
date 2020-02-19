package com.mupper.gobus.viewmodel

import android.graphics.drawable.Drawable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mupper.gobus.commons.Event
import com.mupper.gobus.model.TravelControl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TravelViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockTravelControl: TravelControl

    @Mock
    lateinit var mockEventObserver: Observer<Event<Unit>>

    @Mock
    lateinit var mockTravelStateEventObserver: Observer<Event<TravelViewModel.TravelState>>

    @Mock
    lateinit var mockFabIconLiveDataObserver: Observer<Drawable>

    @Mock
    lateinit var mockFabColorLiveDataObserver: Observer<Int>

    @Mock
    lateinit var mockFabIconColorLiveDataObserver: Observer<Int>

    private lateinit var travelViewModel: TravelViewModel

    @Before
    fun setUp() {
        travelViewModel = TravelViewModel(
            mockTravelControl,
            Dispatchers.Unconfined
        )
    }

    @Test
    fun `toggleTravelState should call navigateToStopTravelDialogLiveData event when travelStateLiveData is OnWay state`() {
        runBlocking {
            // GIVEN
            with (travelViewModel) {
                navigateToStopTravelDialogLiveData.observeForever(mockEventObserver)
                travelStateMutableLiveData.value = Event(TravelViewModel.TravelState.OnWay)

                // WHEN
                toggleTravelState()

                // THEN
                val navigateToStopTravelDialogLiveDataEvent = navigateToStopTravelDialogLiveData.value
                verify(mockEventObserver).onChanged(navigateToStopTravelDialogLiveDataEvent)
            }
        }
    }

    @Test
    fun `toggleTravelState should call navigateToStartTravelDialogLiveData event when travelStateLiveData is Walking state`() {
        runBlocking {
            // GIVEN
            with (travelViewModel) {
                navigateToStartTravelDialogLiveData.observeForever(mockEventObserver)
                travelStateMutableLiveData.value = Event(TravelViewModel.TravelState.Walking)

                // WHEN
                toggleTravelState()

                // THEN
                val navigateToStartTravelDialogLiveDataEvent = navigateToStartTravelDialogLiveData.value
                verify(mockEventObserver).onChanged(navigateToStartTravelDialogLiveDataEvent)
            }
        }
    }

    @Test
    fun `navigateToBusNavigation should call navigateToBusNavigationLiveData event`() {
        runBlocking {
            // GIVEN
            with (travelViewModel) {
                navigateToBusNavigationLiveData.observeForever(mockEventObserver)

                // WHEN
                navigateToBusNavigation()

                // THEN
                verify(mockEventObserver).onChanged(any())
            }
        }
    }

    @Test
    fun `letsWalk should call travelStateMutableLiveData event with Walking TravelState`() {
        runBlocking {
            // GIVEN
            with (travelViewModel) {
                travelStateMutableLiveData.observeForever(mockTravelStateEventObserver)

                // WHEN
                letsWalk()

                // THEN
                val travelStateMutableLiveDataEvent = travelStateMutableLiveData.value
                verify(mockTravelStateEventObserver).onChanged(travelStateMutableLiveDataEvent)
                val travelState = travelStateMutableLiveData.value?.peekContent()
                assertThat(travelState == TravelViewModel.TravelState.Walking, `is`(true))
            }
        }
    }

    @Test
    fun `letsTravel should call travelStateMutableLiveData event with OnWay TravelState`() {
        runBlocking {
            // GIVEN
            with (travelViewModel) {
                travelStateMutableLiveData.observeForever(mockTravelStateEventObserver)

                // WHEN
                letsTravel()

                // THEN
                val travelStateMutableLiveDataEvent = travelStateMutableLiveData.value
                verify(mockTravelStateEventObserver).onChanged(travelStateMutableLiveDataEvent)
                val travelState = travelStateMutableLiveData.value?.peekContent()
                assertThat(travelState == TravelViewModel.TravelState.OnWay, `is`(true))
            }
        }
    }

    @Test
    fun `setFabToStop should call fabIconLiveData with stopIcon`() {
        // GIVEN
        with (travelViewModel) {
            val expectedStopIcon: Drawable = mock()
            given(mockTravelControl.stopIcon).willReturn(expectedStopIcon)
            fabIconLiveData.observeForever(mockFabIconLiveDataObserver)

            // WHEN
            setFabToStop()

            // THEN
            verify(mockFabIconLiveDataObserver).onChanged(expectedStopIcon)
        }
    }

    @Test
    fun `setFabToStop should call fabColorLiveData with defaultFabIconColor`() {
        // GIVEN
        with (travelViewModel) {
            val expectedDefaultFabColor = 1
            given(mockTravelControl.defaultFabIconColor).willReturn(expectedDefaultFabColor)
            fabColorLiveData.observeForever(mockFabColorLiveDataObserver)

            // WHEN
            setFabToStop()

            // THEN
            verify(mockFabColorLiveDataObserver).onChanged(expectedDefaultFabColor)
        }
    }

    @Test
    fun `setFabToStop should call fabIconColorLiveData with defaultFabColor`() {
        // GIVEN
        with (travelViewModel) {
            val expectedDefaultFabColor = 1
            given(mockTravelControl.defaultFabColor).willReturn(expectedDefaultFabColor)
            fabIconColorLiveData.observeForever(mockFabIconColorLiveDataObserver)

            // WHEN
            setFabToStop()

            // THEN
            verify(mockFabIconColorLiveDataObserver).onChanged(expectedDefaultFabColor)
        }
    }

    @Test
    fun `setFabToStart should call fabIconLiveData with playIcon`() {
        // GIVEN
        with (travelViewModel) {
            val expectedPlayIcon: Drawable = mock()
            given(mockTravelControl.playIcon).willReturn(expectedPlayIcon)
            fabIconLiveData.observeForever(mockFabIconLiveDataObserver)

            // WHEN
            setFabToStart()

            // THEN
            verify(mockFabIconLiveDataObserver).onChanged(expectedPlayIcon)
        }
    }

    @Test
    fun `setFabToStart should call fabColorLiveData with defaultFabColor`() {
        // GIVEN
        with (travelViewModel) {
            val expectedDefaultFabColor = 1
            given(mockTravelControl.defaultFabColor).willReturn(expectedDefaultFabColor)
            fabColorLiveData.observeForever(mockFabColorLiveDataObserver)

            // WHEN
            setFabToStart()

            // THEN
            verify(mockFabColorLiveDataObserver).onChanged(expectedDefaultFabColor)
        }
    }

    @Test
    fun `setFabToStart should call fabIconColorLiveData with defaultFabColor`() {
        // GIVEN
        with (travelViewModel) {
            val expectedDefaultFabIconColor = 1
            given(mockTravelControl.defaultFabIconColor).willReturn(expectedDefaultFabIconColor)
            fabIconColorLiveData.observeForever(mockFabColorLiveDataObserver)

            // WHEN
            setFabToStart()

            // THEN
            verify(mockFabColorLiveDataObserver).onChanged(expectedDefaultFabIconColor)
        }
    }
}