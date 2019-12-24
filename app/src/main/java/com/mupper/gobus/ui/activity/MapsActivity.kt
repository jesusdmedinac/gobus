package com.mupper.gobus.ui.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.commons.getCompatColor
import com.mupper.commons.getCompatDrawable
import com.mupper.commons.getViewModel
import com.mupper.commons.scope.ScoppedActivity
import com.mupper.gobus.GobusApp
import com.mupper.gobus.PermissionRequester
import com.mupper.gobus.R
import com.mupper.gobus.databinding.ActivityMapsBinding
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.MapsViewModel.MapsModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelViewModel.TravelModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.launch

class MapsActivity : ScoppedActivity() {

    private lateinit var mapFragment: SupportMapFragment

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var travelerViewModel: TravelerViewModel
    private lateinit var travelViewModel: TravelViewModel

    private val coarseLocationPermissionRequester =
        PermissionRequester(this, ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMapsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_maps)

        val playIcon = getCompatDrawable(R.drawable.ic_start)
        val stopIcon = getCompatDrawable(R.drawable.ic_stop)
        val defaultFabColor = getCompatColor(R.color.colorAccent)
        val defaultFabIconColor = getCompatColor(R.color.white)

        mapsViewModel =
            getViewModel { MapsViewModel(LocationRepository(application)) }
        travelerViewModel =
            getViewModel { TravelerViewModel(TravelerRepository(application as GobusApp)) }
        travelViewModel =
            getViewModel {
                TravelViewModel(
                    playIcon,
                    stopIcon,
                    defaultFabColor,
                    defaultFabIconColor
                )
            }

        binding.travel = travelViewModel
        binding.lifecycleOwner = this

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapsViewModel.model.observe(this, Observer(::onMapsModelChange))
        travelViewModel.model.observe(this, Observer(::onTravelModelChange))
    }

    private fun onMapsModelChange(model: MapsModel) {
        when (model) {
            is MapsModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapsModel.RequestLocationPermissions -> {
                launch {
                    val hasCoarseLocation = coarseLocationPermissionRequester.request()
                    if (hasCoarseLocation) {
                        mapsViewModel.onPermissionsRequested()
                    }
                }
            }
            is MapsModel.RequestNewLocation -> mapsViewModel.onNewLocationRequested()
            is MapsModel.NewLocation -> with(model.lastLocation) {
                if (model.isTrvaling) {
                    travelerViewModel.shareActualLocation(this)
                }
            }
        }
    }

    private fun onTravelModelChange(model: TravelModel) {
        when (model) {
            is TravelModel.StartTravel -> {
                mapsViewModel.startTravel()
                travelerViewModel.startTravel()
            }
            is TravelModel.StopTravel -> {
                mapsViewModel.stopTravel()
                travelerViewModel.stopTravel()
            }
        }
    }
}
