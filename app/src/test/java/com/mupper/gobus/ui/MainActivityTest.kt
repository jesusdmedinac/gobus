@file:Suppress("DEPRECATION")

package com.mupper.gobus.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.runner.AndroidJUnit4
import com.mupper.gobus.DEPENDENCY_NAME_UI_DISPATCHER
import com.mupper.gobus.R
import com.mupper.gobus.initMockedDi
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class MainActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

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
    fun `validate that first destination is MapsFragment`() {
        // GIVEN
        val scenario = launch(MainActivity::class.java)

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)

        // THEN
        scenario.onActivity { activity ->
            val navController = activity.findNavController(R.id.main_nav_host_fragment)
            assertThat(navController.currentDestination?.label.toString(), `is`("MapsFragment"))
        }
    }
}