package com.mupper.gobus.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.gobus.PermissionRequester
import com.mupper.gobus.R
import com.mupper.gobus.commons.EventObserver
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.commons.extension.navigateTo
import com.mupper.gobus.commons.extension.supportFragmentManager
import com.mupper.gobus.databinding.FragmentMapsBinding
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.MapViewModel.MapModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import org.koin.android.ext.android.inject

class MapsFragment : Fragment() {
    private val mapViewModel: MapViewModel by inject()
    private val travelViewModel: TravelViewModel by inject()
    private val travelerViewModel: TravelerViewModel by inject()

    private lateinit var mapFragment: SupportMapFragment

    private val locationPermissionRequester by lazy {
        PermissionRequester(
            activity!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private var binding: FragmentMapsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_maps, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager.findFragmentById(R.id.map).let {
            mapFragment = it as SupportMapFragment
        }

        initObservers()

        binding?.apply {
            travel = travelViewModel
            lifecycleOwner = this@MapsFragment
        }
    }

    private fun initObservers() {
        mapViewModel.mapEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver(::onMapModelChange)
        )

        mapViewModel.requestLocationPermissionEventLiveData.observe(viewLifecycleOwner,
            EventObserver {
                locationPermissionRequester.request {
                    if (it)
                        mapViewModel.onPermissionsRequested()
                }
            })

        travelViewModel.navigateToStartTravelDialogLiveData.observe(viewLifecycleOwner,
            EventObserver {
                val toStartTravelDialogFragment: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToStartTravelDialogFragment()
                navigateTo(toStartTravelDialogFragment)
            })
        travelViewModel.navigateToStopTravelDialogLiveData.observe(viewLifecycleOwner,
            EventObserver {
                val toStopTravelDialogFragment: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToStopTravelDialogFragment()
                navigateTo(toStopTravelDialogFragment)
            })

        travelViewModel.travelStateLiveData.observe(
            viewLifecycleOwner,
            EventObserver(::onTravelModelChange)
        )
    }

    override fun onResume() {
        mapViewModel.requestLocationPermission()
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapViewModel.clearMap()
        supportFragmentManager.beginTransaction().remove(mapFragment).commitAllowingStateLoss()
    }

    private fun onMapModelChange(model: MapModel) {
        when (model) {
            is MapModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapModel.RequestNewLocation -> mapViewModel.onNewLocationRequested()
            is MapModel.NewLocation -> with(model) {
                if (isTraveling) {
                    travelerViewModel.shareActualLocation(lastLocation)
                }
            }
        }
    }

    private fun onTravelModelChange(state: TravelViewModel.TravelState) {
        when (state) {
            is TravelViewModel.TravelState.Walking -> {
                mapViewModel.stopTravel()
                travelViewModel.setFabToStart()
            }
            is TravelViewModel.TravelState.OnWay -> {
                mapViewModel.startTravel()
                travelViewModel.setFabToStop()
            }
        }
    }
}
