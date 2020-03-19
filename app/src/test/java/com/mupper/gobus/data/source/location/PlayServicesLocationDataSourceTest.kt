package com.mupper.gobus.data.source.location

import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.mupper.gobus.model.PermissionChecker
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayServicesLocationDataSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockFusedLocationClient: FusedLocationProviderClient

    @Mock
    private lateinit var mockFineLocationPermissionChecker: PermissionChecker

    @Mock
    private lateinit var mockLocationManager: LocationManager

    @Mock
    private lateinit var mockLooper: Looper

    private lateinit var playServicesLocationDataSource: PlayServicesLocationDataSource

    @Before
    fun setUp() {
        playServicesLocationDataSource = PlayServicesLocationDataSource(
            mockFusedLocationClient,
            mockFineLocationPermissionChecker,
            mockLocationManager,
            mockLooper
        )
    }

    @Test
    fun `findLastLocation should return null when canAccessLocation return false`() {
        runBlocking {
            // GIVEN
            with(playServicesLocationDataSource) {
                // WHEN
                val lastLocation = findLastLocation()

                // THEN
                assertThat(lastLocation, `is`(nullValue()))
            }
        }
    }

    @Test
    @Ignore("I don't know how to test mock suspendCancellableCoroutine")
    fun `findLastLocation should call lastLocation of fusedLocationClient`() {
        runBlocking {
            // GIVEN
            with(playServicesLocationDataSource) {
                given(mockLocationManager.isProviderEnabled(any())).willReturn(true)
                given(mockLocationManager.isProviderEnabled(any())).willReturn(true)
                given(mockFineLocationPermissionChecker.check()).willReturn(true)
                val lastLocation = mock<Task<Location>>()
                given(lastLocation.addOnCompleteListener(any())).willReturn(mock())
                given(mockFusedLocationClient.lastLocation).willReturn(lastLocation)

                // WHEN
                findLastLocation()

                // THEN
                verify(mockFusedLocationClient).lastLocation
            }
        }
    }

    @Test
    fun `requestLocationUpdates should not interact with fusedLocationClient when canAccessLocation return false`() {
        runBlocking {
            // GIVEN
            with(playServicesLocationDataSource) {
                // WHEN
                requestLocationUpdates(mock(), mock())

                // THEN
                verifyZeroInteractions(mockFusedLocationClient)
            }
        }
    }

    @Test
    fun `requestLocationUpdates should call requestLocationUpdates of fusedLocationClient when canAccessLocation return true`() {
        runBlocking {
            // GIVEN
            with(playServicesLocationDataSource) {
                given(mockLocationManager.isProviderEnabled(any())).willReturn(true)
                given(mockFineLocationPermissionChecker.check()).willReturn(true)

                // WHEN
                requestLocationUpdates(mock(), mock())

                // THEN
                verify(mockFusedLocationClient).requestLocationUpdates(any(), any(), any())
            }
        }
    }
}