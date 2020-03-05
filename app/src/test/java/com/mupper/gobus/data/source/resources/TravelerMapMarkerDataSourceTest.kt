package com.mupper.gobus.data.source.resources

import android.os.Handler
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.gobus.data.mapper.toMapsLatLng
import com.mupper.sharedtestcode.fakeLatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.willDoNothing
import org.mockito.BDDMockito.willReturn
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TravelerMapMarkerDataSourceTest {

    @Mock
    lateinit var mockMapMarkerOptions: MarkerOptions

    @Mock
    lateinit var mockMapMarker: Marker

    @Mock
    lateinit var mockBusIcon: BitmapDescriptor

    private lateinit var travelerMapMarkerDataSource: TravelerMapMarkerDataSource

    @Before
    fun setUp() {
        travelerMapMarkerDataSource = TravelerMapMarkerDataSource(mockBusIcon)
    }

    @Test
    fun `moveMarkerToLastLocation should initialize mapMarkerOptions when mapMarkerOptions is null`() {
        // GIVEN
        val fakeLatLng = fakeLatLng.copy()

        // WHEN
        with(travelerMapMarkerDataSource) {
            moveMarkerToLastLocation({}, fakeLatLng) { mockMapMarker }

            // THEN
            val expectedMapMarkerOptions = mapMarkerOptions
            assertThat(expectedMapMarkerOptions, `is`(notNullValue()))
        }
    }

    @Test
    fun `moveMarkerToLastLocation should initialize mapMarker when mapMarker is null`() {
        // GIVEN
        val fakeLatLng = fakeLatLng.copy()

        // WHEN
        with(travelerMapMarkerDataSource) {
            moveMarkerToLastLocation({}, fakeLatLng) { mockMapMarker }

            // THEN
            val expectedMapMarker = mapMarker
            assertThat(expectedMapMarker, `is`(notNullValue()))
        }
    }

    @Test
    fun `moveMarkerToLastLocation should change mapMarkerOptions position to expected LatLng when mapMarkerOptions and mapMarker are not null`() {
        // GIVEN
        with(travelerMapMarkerDataSource) {
            val expectedLatLng = fakeLatLng.copy(1.0, 1.0)
            mapMarkerOptions = spy(mockMapMarkerOptions)
            mapMarker = mockMapMarker

            // WHEN
            moveMarkerToLastLocation({}, expectedLatLng) { mapMarker }

            // THEN
            assertThat(mapMarkerOptions?.position, `is`(expectedLatLng.toMapsLatLng()))
        }
    }

    @Test
    fun `moveMarkerToLastLocation should change mapMarker position to expected LatLng when mapMarkerOptions and mapMarker are not null`() {
        // GIVEN
        with(travelerMapMarkerDataSource) {
            val expectedLatLng = fakeLatLng.copy(1.0, 1.0)
            val spyMapMarker = spy(mockMapMarker)
            willDoNothing().given(spyMapMarker).position = expectedLatLng.toMapsLatLng()
            willReturn(expectedLatLng.toMapsLatLng()).given(spyMapMarker).position
            willDoNothing().given(spyMapMarker).isVisible = false
            mapMarkerOptions = mockMapMarkerOptions
            mapMarker = spyMapMarker

            // WHEN
            moveMarkerToLastLocation({}, expectedLatLng) { mapMarker }

            // THEN
            assertThat(mapMarker?.position, `is`(expectedLatLng.toMapsLatLng()))
        }
    }

    @Test
    fun `shouldDrawMarker should set mapMarker visibility as true when visibleForMap is true`() {
        //GIVEN
        val spyTravelerMapMarkerDataSource = spy(travelerMapMarkerDataSource)
        with(spyTravelerMapMarkerDataSource) {
            visibleForMap = true
            val spyMapMarker = spy(mockMapMarker)
            willDoNothing().given(spyMapMarker).isVisible = true
            willReturn(true).given(spyMapMarker).isVisible
            willDoNothing().given(this).smoothMoveMarker(any())
            mapMarker = spyMapMarker

            // WHEN
            moveMarkerToLastLocation({}, fakeLatLng) { spyMapMarker }

            // THEN
            assertThat(mapMarker?.isVisible, `is`(true))
        }
    }

    @Test
    fun `smoothMoveMarker should call mapMarker position`() {
        // GIVEN
        val expectedLatLng = fakeLatLng.copy(1.0, 1.0)
        val spyMapMarker = spy(mockMapMarker)
        willReturn(expectedLatLng.toMapsLatLng()).given(spyMapMarker).position
        val spyTravelerMapMarkerDataSource = spy(travelerMapMarkerDataSource)
        with(spyTravelerMapMarkerDataSource) {
            val fakeTimestamp = 1581921076L
            willReturn(fakeTimestamp).given(this).getSystemClockUptimeMillis()
            val mockHandler: Handler = mock()
            willReturn(false).given(mockHandler).post(any())
            willReturn(mockHandler).given(this).getHandler()
            mapMarker = spyMapMarker

            // WHEN
            smoothMoveMarker(expectedLatLng)

            // THEN
            verify(spyMapMarker).position
        }
    }
}