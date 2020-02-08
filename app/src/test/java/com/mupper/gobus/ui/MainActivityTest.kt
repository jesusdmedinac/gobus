package com.mupper.gobus.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.runner.AndroidJUnit4
import com.mupper.gobus.R
import com.mupper.gobus.initMockedDi
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class MainActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        initMockedDi()
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