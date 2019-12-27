package com.mupper.gobus.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.commons.scope.ScoppedFragment
import com.mupper.gobus.PermissionRequester
import com.mupper.gobus.R
import com.mupper.gobus.commons.EventObserver
import com.mupper.gobus.commons.app
import com.mupper.gobus.commons.getViewModel
import com.mupper.gobus.commons.supportFragmentManager
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.MapsViewModel.MapsModel
import com.mupper.gobus.viewmodel.TravelerViewModel

class MapsFragment() : ScoppedFragment() {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var travelerViewModel: TravelerViewModel

    private lateinit var mapFragment: SupportMapFragment

    private val coarsePermissionRequester by lazy {
        PermissionRequester(
            activity!!,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsViewModel =
            getViewModel { MapsViewModel(LocationRepository(app)) }
        travelerViewModel =
            getViewModel { TravelerViewModel(TravelerRepository(app)) }


        childFragmentManager.findFragmentById(R.id.map).let {
            mapFragment = it as SupportMapFragment
        }

        mapsViewModel.model.observe(this, Observer(::onMapsModelChange))

        mapsViewModel.requestCoarsePermission.observe(this, EventObserver {
            coarsePermissionRequester.request {
                mapsViewModel.onPermissionsRequested()
            }
        })
    }

    private fun onMapsModelChange(model: MapsModel) {
        when (model) {
            is MapsModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapsModel.RequestNewLocation -> mapsViewModel.onNewLocationRequested()
            is MapsModel.NewLocation -> with(model.lastLocation) {
                if (model.isTrvaling) {
                    travelerViewModel.shareActualLocation(this)
                }
            }
        }
    }
}