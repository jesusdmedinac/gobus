@file:Suppress("DEPRECATION")

package com.mupper.gobus.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import com.mupper.gobus.DEPENDENCY_NAME_UI_DISPATCHER
import com.mupper.gobus.R
import com.mupper.gobus.utils.initMockedDi
import com.mupper.gobus.utils.launchMainActivity
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        val vmModule = module {
            factory { MapViewModel(get(), get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            factory { TravelerViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            factory { TravelViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)
    }

    @Test
    fun `first destination is MapsFragment`() {
        // WHEN
        launchMainActivity()

        // THEN
        onView(withId(R.id.btnToggleTravel)).check(matches(isDisplayed()))
    }
}