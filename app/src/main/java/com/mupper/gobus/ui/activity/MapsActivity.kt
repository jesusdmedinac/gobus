package com.mupper.gobus.ui.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.commons.getViewModel
import com.mupper.core.database.GobusDatabase
import com.mupper.gobus.GobusApp
import com.mupper.gobus.PermissionRequester
import com.mupper.gobus.R
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.scope.Scope
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.MapsViewModel.MapsModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), Scope by Scope.Impl() {

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var travelerViewModel: TravelerViewModel

    private val coarseLocationPermissionRequester =
        PermissionRequester(this, ACCESS_COARSE_LOCATION)
    private val fineLocationPermissionRequester = PermissionRequester(this, ACCESS_FINE_LOCATION)

    init {
        initScope()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapsViewModel =
            getViewModel { MapsViewModel(LocationRepository(application)) }
        travelerViewModel =
            getViewModel { TravelerViewModel(TravelerRepository(application as GobusApp)) }

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapsViewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: MapsModel) {
        when (model) {
            is MapsModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapsModel.RequestLocationPermissions -> {
                launch {
                    val hasCoarseLocation = coarseLocationPermissionRequester.request()
                    val hasFineLocation = fineLocationPermissionRequester.request()
                    if (hasCoarseLocation && hasFineLocation) {
                        mapsViewModel.onPermissionsRequested()
                    }
                }
            }
            is MapsModel.RequestNewLocation -> mapsViewModel.onNewLocationRequested()
            is MapsModel.NewLocation -> with(model.lastLocation) {
                travelerViewModel.shareActualLocation(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyScope()
    }
}
