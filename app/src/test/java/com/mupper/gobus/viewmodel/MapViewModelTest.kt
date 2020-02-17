package com.mupper.gobus.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.mupper.data.source.location.LocationDataSource
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.extension.getOrAwaitValue
import com.mupper.gobus.data.source.resources.TravelerMapMarkerDataSource
import com.mupper.sharedtestcode.fakeLatLng
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var locationDataSource: LocationDataSource<LocationRequest, LocationCallback>

    @Mock
    lateinit var travelerMapMarkerDataSource: TravelerMapMarkerDataSource

    @Mock
    lateinit var mapEventLiveDataObserver: Observer<Event<MapViewModel.MapsModel>>

    @Mock
    lateinit var eventObserver: Observer<Event<Unit>>

    @Mock
    lateinit var googleMaps: GoogleMap

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        mapViewModel =
            MapViewModel(locationDataSource, travelerMapMarkerDataSource, Dispatchers.Unconfined)
    }

    @Test
    fun `MapsViewModel should not interact with mapsEventLiveData on init`() {
        // GIVEN
        mapViewModel.mapsEventLiveData.observeForever(mapEventLiveDataObserver)

        // WHEN MapsViewModel init

        // THEN
        verifyZeroInteractions(mapEventLiveDataObserver)
    }

    @Test
    fun `requestLocationPermission should call requestLocationPermissionEventLiveData event`() {
        runBlocking {
            with(mapViewModel) {
                // GIVEN
                requestLocationPermissionEventLiveData.observeForever(eventObserver)

                // WHEN
                requestLocationPermission()

                // THEN
                val expectedRequestLocationPermissionEventLiveData =
                    requestLocationPermissionEventLiveData.value
                verify(eventObserver).onChanged(expectedRequestLocationPermissionEventLiveData)
            }
        }
    }

    @Test
    fun `onPermissionRequest should call mapEventLiveData with MapReady event`() {
        with(mapViewModel) {
            // GIVEN
            mapsEventLiveData.observeForever(mapEventLiveDataObserver)

            // WHEN
            onPermissionsRequested()

            // THEN
            val expectedMapsModelEvent = mapsEventLiveData.value
            verify(mapEventLiveDataObserver).onChanged(expectedMapsModelEvent)
        }
    }

    @Test
    fun `onPermissionRequest should call initGoogleMap with expected GoogleMap`() {
        // GIVEN
        val spyMapsViewModel = spy(mapViewModel)
        with(spyMapsViewModel) {
            // WHEN
            onPermissionsRequested()
            val mapsEvent =
                mapsEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapsModel.MapReady
            mapsEvent.onMapReady.onMapReady(googleMaps)

            // THEN
            verify(spyMapsViewModel).initGoogleMap(googleMaps)
        }
    }

    @Test
    fun `onNewLocationRequested should call findLastLocation of locationDataSource`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                // WHEN
                onNewLocationRequested()

                // THEN
                verify(locationDataSource).findLastLocation()
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                given(locationDataSource.findLastLocation()).willReturn(expectedLatLng)

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapsEvent = mapsEventLiveData.getOrAwaitValue().peekContent()
                assertThat(mapsEvent, instanceOf(MapViewModel.MapsModel.NewLocation::class.java))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with expected LatLng`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy(2.0, 2.0)
                given(locationDataSource.findLastLocation()).willReturn(expectedLatLng)

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapsEvent =
                    mapsEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapsModel.NewLocation
                assertThat(mapsEvent.lastLocation, `is`(expectedLatLng))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with isTraveling property as false`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                given(locationDataSource.findLastLocation()).willReturn(expectedLatLng)
                stopTravel()

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapsEvent =
                    mapsEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapsModel.NewLocation
                assertThat(mapsEvent.isTraveling, `is`(false))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with isTraveling property as true`() {
        runBlocking {
            // GIVEN
            val spyMapsViewModel = spy(mapViewModel)
            with(spyMapsViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                given(locationDataSource.findLastLocation()).willReturn(expectedLatLng)
                val expectedNewLatLngZoom: CameraUpdate = mock()
                willReturn(expectedNewLatLngZoom).given(this).generateNewLatLngZoom(expectedLatLng)
                willReturn(true).given(travelerMapMarkerDataSource).visibleForMap
                willDoNothing().given(travelerMapMarkerDataSource).visibleForMap = true
                startTravel()

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapsEvent =
                    mapsEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapsModel.NewLocation
                assertThat(mapsEvent.isTraveling, `is`(true))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call googleMap animateCamera with any argument`() {
        runBlocking {
            // GIVEN
            val spyMapViewModel = spy(mapViewModel)
            with(spyMapViewModel) {
                // Map is ready
                onPermissionsRequested()
                val mapsEvent =
                    mapsEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapsModel.MapReady
                mapsEvent.onMapReady.onMapReady(googleMaps)
                // findLastLocation of locationDataSource return expectedLatLng
                val expectedLatLng = fakeLatLng.copy()
                given(locationDataSource.findLastLocation()).willReturn(expectedLatLng)
                // generateNewLatLngZoom return expectedNewLatLngZoom
                val expectedNewLatLngZoom: CameraUpdate = mock()
                will { expectedNewLatLngZoom }.given(this).generateNewLatLngZoom(expectedLatLng)

                // WHEN
                onNewLocationRequested()

                // THEN
                verify(googleMaps).animateCamera(any())
            }
        }
    }
}