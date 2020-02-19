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
import com.mupper.gobus.commons.extension.navigate
import com.mupper.gobus.commons.extension.supportFragmentManager
import com.mupper.gobus.databinding.FragmentMapsBinding
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.MapViewModel.MapsModel
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
        mapViewModel.mapsEventLiveData.observe(
            viewLifecycleOwner,
            EventObserver(::onMapsModelChange)
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
                    MapsFragmentDirections.actionMapsFragmentToStartTravelFragment()
                navigate(toStartTravelDialogFragment)
            })
        travelViewModel.navigateToStopTravelDialogLiveData.observe(viewLifecycleOwner,
            EventObserver {
                val toStopTravelDialogFragment: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToStopTravelFragment()
                navigate(toStopTravelDialogFragment)
            })

        travelViewModel.travelStateLiveData.observe(
            viewLifecycleOwner,
            EventObserver(::onTravelModelChange)
        )
    }

    override fun onResume() {
        super.onResume()
        mapViewModel.requestLocationPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapViewModel.clearMap()
        supportFragmentManager.beginTransaction().remove(mapFragment).commit()
    }

    private fun onMapsModelChange(model: MapsModel) {
        when (model) {
            is MapsModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapsModel.RequestNewLocation -> mapViewModel.onNewLocationRequested()
            is MapsModel.NewLocation -> with(model.lastLocation) {
                if (model.isTraveling) {
                    travelerViewModel.shareActualLocation(this)
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
