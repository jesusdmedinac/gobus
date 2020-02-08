package com.mupper.gobus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import com.mupper.gobus.commons.extension.getOrAwaitValue
import com.mupper.gobus.commons.extension.observeForTesting
import com.mupper.gobus.initMockedDi
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
class MapsViewModelTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mapsViewModel: MapsViewModel by inject()

    @Before
    fun setUp() {
        initMockedDi()
    }

    @Test
    fun `MapsViewModel should request location permission`() {
        // GIVEN
        val notExpectedRequestLocationPermissionValue = mapsViewModel.requestLocationPermission.value

        // WHEN
        mapsViewModel.requestLocationPermission()

        // THEN
        val expectedRequestLocationPermissionValue = mapsViewModel.requestLocationPermission.value
        assertThat(notExpectedRequestLocationPermissionValue, not(expectedRequestLocationPermissionValue))
    }

    @Test
    fun `MapsViewModel should call initGoogleMap when onPermissionRequested is call`() {
        // GIVEN

        // WHEN
        mapsViewModel.onPermissionsRequested()

        // THEN
        mapsViewModel.model.observeForTesting {

        }
    }

    // TODO: ViewModel should call initGoogleMap when onPermissionRequested is call
}