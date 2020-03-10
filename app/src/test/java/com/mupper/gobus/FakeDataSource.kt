package com.mupper.gobus

import android.graphics.Color
import android.graphics.drawable.Drawable
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mupper.data.source.local.BusLocalDataSource
import com.mupper.data.source.local.TravelerLocalDataSource
import com.mupper.data.source.location.LocationDataSource
import com.mupper.data.source.remote.BusRemoteDataSource
import com.mupper.data.source.remote.TravelerRemoteDataSource
import com.mupper.data.source.resources.MapMarkerDataSource
import com.mupper.data.source.resources.MapResourcesDataSource
import com.mupper.data.source.resources.TravelControlDataSource
import com.mupper.domain.LatLng
import com.mupper.domain.bus.Bus
import com.mupper.domain.relations.BusWithTravelers
import com.mupper.domain.resources.BusIcon
import com.mupper.domain.traveler.Traveler
import com.mupper.sharedtestcode.fakeBus
import com.mupper.sharedtestcode.fakeLatLng
import com.mupper.sharedtestcode.fakeTraveler
import com.nhaarman.mockitokotlin2.mock

class FakeMapResourcesDataSource : MapResourcesDataSource<BitmapDescriptor> {
    override var busIcon = BusIcon(0, 0)

    override fun getBusIcon(): BitmapDescriptor =
        BitmapDescriptorFactory.defaultMarker()
}

class FakeTravelControl : TravelControlDataSource<Drawable> {
    override val playIcon: Drawable
        get() = Drawable.createFromXml(app.resources, app.resources.getXml(R.drawable.ic_stop))
    override val stopIcon: Drawable
        get() = Drawable.createFromXml(app.resources, app.resources.getXml(R.drawable.ic_stop))
    override val defaultFabColor: Int
        get() = Color.GREEN
    override val defaultFabIconColor: Int
        get() = Color.GREEN
}

class FakeLocationDataSource :
    LocationDataSource<LocationRequest, LocationCallback> {
    var lastLocation: LatLng? = fakeLatLng.copy()

    override suspend fun findLastLocation(): LatLng? = lastLocation

    override fun requestLocationUpdates(
        locationRequest: LocationRequest,
        locationCallback: LocationCallback
    ) {
    }
}

class FakeBusLocalDataSource : BusLocalDataSource {
    private var travelingBusWithTravelersList: List<BusWithTravelers> = emptyList()

    override suspend fun getBusCount(path: String): Int = travelingBusWithTravelersList.size

    override suspend fun getTravelingBusWithTravelers(): List<BusWithTravelers> =
        travelingBusWithTravelersList

    override suspend fun addNewBus(bus: Bus) {}

    override suspend fun shareActualLocation(bus: BusWithTravelers) {}
}

class FakeBusRemoteDataSource : BusRemoteDataSource {
    private var bus: Bus = fakeBus.copy()

    private var travelerList: List<Traveler> = emptyList()

    override fun addNewBusWithTravelers(bus: Bus, traveler: Traveler) {}

    override suspend fun findBusByPathName(path: String): Bus = bus

    override suspend fun findBusTravelersByPathName(path: String): List<Traveler> = travelerList

    override suspend fun shareActualLocation(bus: BusWithTravelers, traveler: Traveler) {}
}

class FakeTravelerLocalDataSource : TravelerLocalDataSource {
    var traveler = fakeTraveler.copy()

    var travelerCount = 1

    override suspend fun getTravelerCount(): Int = travelerCount

    override suspend fun insertTraveler(travelingPath: String, traveler: Traveler) {}

    override suspend fun findTravelerByEmail(email: String): Traveler? = traveler

    override suspend fun shareActualLocation(traveler: Traveler) {}
}

class FakeTravelerRemoteDataSource : TravelerRemoteDataSource {
    var traveler: Traveler? = fakeTraveler.copy()

    override suspend fun addTraveler(traveler: Traveler): Traveler = traveler

    override suspend fun findTravelerByEmail(email: String): Traveler? = traveler

    override suspend fun shareActualLocation(traveler: Traveler) {}
}

class FakeMapMarkerDataSource : MapMarkerDataSource<Marker, MarkerOptions> {
    override var mapMarker: Marker? = mock()

    override var mapMarkerOptions: MarkerOptions? = mock()

    override var visibleForMap: Boolean = false

    override fun moveMarkerToLastLocation(
        enableUserLocation: (Boolean) -> Unit,
        lastLatLng: LatLng,
        addMarkerToMap: (MarkerOptions) -> Marker?
    ): LatLng = fakeLatLng.copy()
}
