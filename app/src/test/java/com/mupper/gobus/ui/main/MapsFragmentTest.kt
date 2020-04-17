package com.mupper.gobus.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.ActionOnlyNavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import com.mupper.gobus.DEPENDENCY_NAME_UI_DISPATCHER
import com.mupper.gobus.R
import com.mupper.gobus.commons.Event
import com.mupper.gobus.utils.findNavController
import com.mupper.gobus.utils.initMockedDi
import com.mupper.gobus.utils.launchMainActivity
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MapsFragmentTest : AutoCloseKoinTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var travelViewModel: TravelViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            single { MapViewModel(get(), get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            single { TravelerViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            single { TravelViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)
        travelViewModel = get()
    }

    @Test
    fun `btnToggleTravel is displayed`() {
        // GIVEN
        // WHEN
        launchMainActivity()

        // THEN
        onView(withId(R.id.btnToggleTravel)).check(matches(isDisplayed()))
    }

    @Test
    fun `btnToggleTravel click should navigate to startTravelFragment`() {
        // GIVEN
        val navController = findNavController()
        launchMainActivity()

        // WHEN
        onView(withId(R.id.btnToggleTravel)).perform(click())

        // THEN
        verify {
            navController.navigate(ActionOnlyNavDirections(R.id.action_mapsFragment_to_startTravelDialogFragment))
        }
    }

    @Test
    fun `btnToggleTravel click should navigate to stopTravelDialogFragment when travelState is OnWay`() {
        // GIVEN
        val navController = findNavController()
        launchMainActivity()
        travelViewModel.travelStateMutableLiveData.value = Event(TravelViewModel.TravelState.OnWay)

        // WHEN
        onView(withId(R.id.btnToggleTravel)).perform(click())

        // THEN
        verify {
            navController.navigate(ActionOnlyNavDirections(R.id.action_mapsFragment_to_stopTravelDialogFragment))
        }
    }
}
