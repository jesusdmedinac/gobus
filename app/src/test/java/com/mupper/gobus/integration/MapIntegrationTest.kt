@file:Suppress("DEPRECATION")

package com.mupper.gobus.integration

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.data.source.location.LocationDataSource
import com.mupper.data.source.resources.MapMarkerDataSource
import com.mupper.gobus.*
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.extension.getOrAwaitValue
import com.mupper.gobus.utils.FakeLocationDataSource
import com.mupper.gobus.utils.FakeMapMarkerDataSource
import com.mupper.gobus.utils.initMockedDi
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.sharedtestcode.fakeLatLng
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MapIntegrationTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var spyFakeLocationDataSource: FakeLocationDataSource

    private lateinit var spyFakeMapMarkerDataSource: FakeMapMarkerDataSource

    @Mock
    lateinit var mockGoogleMap: GoogleMap

    @Mock
    lateinit var mockMapEventLiveDataObserver: Observer<Event<MapViewModel.MapModel>>

    @Mock
    lateinit var mockEventObserver: Observer<Event<Unit>>

    private lateinit var mapViewModel: MapViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val vmModule = module {
            factory { MapViewModel(get(), get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)
        mapViewModel = get()
        spyFakeLocationDataSource =
            get<LocationDataSource<LocationRequest, LocationCallback>>() as FakeLocationDataSource
        spyFakeMapMarkerDataSource =
            get<MapMarkerDataSource<Marker, MarkerOptions>>() as FakeMapMarkerDataSource
    }

    @Test
    fun `MapViewModel should not interact with mapEventLiveData on init `() {
        // GIVEN
        mapViewModel.mapEventLiveData.observeForever(mockMapEventLiveDataObserver)

        // WHEN MapViewModel init

        // THEN
        verifyZeroInteractions(mockMapEventLiveDataObserver)
    }

    @Test
    fun `requestLocationPermission should call requestLocationPermissionEventLiveData event`() {
        runBlocking {
            with(mapViewModel) {
                // GIVEN
                requestLocationPermissionEventLiveData.observeForever(mockEventObserver)

                // WHEN
                requestLocationPermission()

                // THEN
                val expectedRequestLocationPermissionEventLiveData =
                    requestLocationPermissionEventLiveData.value
                verify(mockEventObserver).onChanged(expectedRequestLocationPermissionEventLiveData)
            }
        }
    }

    @Test
    fun `onPermissionRequest should call mapEventLiveData with MapReady event`() {
        with(mapViewModel) {
            // GIVEN
            mapEventLiveData.observeForever(mockMapEventLiveDataObserver)

            // WHEN
            onPermissionsRequested()

            // THEN
            val expectedMapModelEvent = mapEventLiveData.value
            verify(mockMapEventLiveDataObserver).onChanged(expectedMapModelEvent)
        }
    }

    @Test
    fun `onPermissionRequest should call initGoogleMap with expected GoogleMap`() {
        runBlocking {
            // GIVEN
            val spyMapViewModel = spy(mapViewModel)
            with(spyMapViewModel) {
                // WHEN
                onPermissionsRequested()
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.MapReady
                mapEvent.onMapReady.onMapReady(mockGoogleMap)

                // THEN
                verify(spyMapViewModel).initGoogleMap(mockGoogleMap)
            }
        }
    }

    @Test
    fun `onPermissionRequest should call requestLocationUpdates of locationDataSource with any arguments`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {

                // WHEN
                onPermissionsRequested()
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.MapReady
                mapEvent.onMapReady.onMapReady(mockGoogleMap)

                // THEN
                verify(spyFakeLocationDataSource).requestLocationUpdates(
                    com.mupper.gobus.utils.any(),
                    com.mupper.gobus.utils.any()
                )
            }
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
                verify(spyFakeLocationDataSource).findLastLocation()
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                spyFakeLocationDataSource.lastLocation = expectedLatLng

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapEvent = mapEventLiveData.getOrAwaitValue().peekContent()
                assertThat(
                    mapEvent,
                    instanceOf(MapViewModel.MapModel.NewLocation::class.java)
                )
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with expected LatLng`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy(2.0, 2.0)
                spyFakeLocationDataSource.lastLocation = expectedLatLng

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.NewLocation
                assertThat(mapEvent.lastLocation, `is`(expectedLatLng))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with isTraveling property as false`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                spyFakeLocationDataSource.lastLocation = expectedLatLng
                stopTravel()

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.NewLocation
                assertThat(mapEvent.isTraveling, `is`(false))
            }
        }
    }

    @Test
    fun `onNewLocationRequest should call mapEventLiveData with NewLocation event with isTraveling property as true`() {
        runBlocking {
            // GIVEN
            val spyMapViewModel = spy(mapViewModel)
            with(spyMapViewModel) {
                val expectedLatLng = fakeLatLng.copy()
                val expectedNewLatLngZoom: CameraUpdate = mock()
                willReturn(expectedNewLatLngZoom).given(this).generateNewLatLngZoom(expectedLatLng)
                spyFakeMapMarkerDataSource.visibleForMap = true
                startTravel()

                // WHEN
                onNewLocationRequested()

                // THEN
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.NewLocation
                assertThat(mapEvent.isTraveling, `is`(true))
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
                val mapEvent =
                    mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.MapReady
                mapEvent.onMapReady.onMapReady(mockGoogleMap)
                // findLastLocation of locationDataSource return expectedLatLng
                val expectedLatLng = fakeLatLng.copy()
                spyFakeLocationDataSource.lastLocation = expectedLatLng
                // generateNewLatLngZoom return expectedNewLatLngZoom
                val expectedNewLatLngZoom: CameraUpdate = mock()
                willReturn(expectedNewLatLngZoom).given(this).generateNewLatLngZoom(expectedLatLng)

                // WHEN
                onNewLocationRequested()

                // THEN
                verify(mockGoogleMap).animateCamera(com.mupper.gobus.utils.any())
            }
        }
    }

    @Test
    fun `clearMap should set googleMap as null`() {
        // GIVEN
        with(mapViewModel) {
            // Map is ready
            onPermissionsRequested()
            val mapEvent =
                mapEventLiveData.getOrAwaitValue().peekContent() as MapViewModel.MapModel.MapReady
            mapEvent.onMapReady.onMapReady(mockGoogleMap)

            //WHEN
            clearMap()

            // THEN
            assertThat(googleMap, `is`(nullValue()))
        }
    }

    @Test
    fun `clearMap should call clearMarker of travelerMapMarkerDataSource`() {
        // GIVEN
        with(mapViewModel) {
            //WHEN
            clearMap()

            // THEN
            verify(spyFakeMapMarkerDataSource).clearMarker()
        }
    }

    @Test
    fun `onLocationResult should not call mapEventLiveData with NewLocation event`() {
        runBlocking {
            // GIVEN
            val expectedLocationResult = null

            // WHEN
            val spyMapViewModel = spy(mapViewModel)
            with(spyMapViewModel) {
                onLocationResult(expectedLocationResult)

                val mapEvent = mapEventLiveData.value
                assertThat(mapEvent, `is`(nullValue()))
            }
        }
    }

    @Test
    fun `onLocationResult should call mapEventLiveData with NewLocation event`() {
        runBlocking {
            // GIVEN
            with(mapViewModel) {
                val mockLocation: Location = mock()
                val expectedLocationResult = LocationResult.create(listOf(mockLocation))

                // WHEN
                launch {
                    onLocationResult(expectedLocationResult)
                }

                val mapEvent = mapEventLiveData.getOrAwaitValue().peekContent()
                assertThat(
                    mapEvent,
                    instanceOf(MapViewModel.MapModel.NewLocation::class.java)
                )
            }
        }
    }
}