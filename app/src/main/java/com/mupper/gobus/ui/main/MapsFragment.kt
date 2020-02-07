package com.mupper.gobus.ui.main

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import com.google.android.gms.maps.SupportMapFragment
import com.mupper.commons.scope.ScoppedFragment
import com.mupper.gobus.PermissionRequester
import com.mupper.gobus.R
import com.mupper.gobus.commons.EventObserver
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.commons.extension.navigate
import com.mupper.gobus.commons.extension.supportFragmentManager
import com.mupper.gobus.databinding.FragmentMapsBinding
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.MapsViewModel.MapsModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

class MapsFragment : ScoppedFragment(Dispatchers.Main) {

    private val mapsViewModel: MapsViewModel by inject()
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
        mapsViewModel.model.observe(
            this,
            EventObserver(::onMapsModelChange)
        )

        mapsViewModel.requestLocationPermission.observe(this,
            EventObserver {
                locationPermissionRequester.request {
                    if (it)
                        mapsViewModel.onPermissionsRequested()
                }
            })

        travelViewModel.navigateToStartTravelDialog.observe(this,
            EventObserver {
                val toStartTravel: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToStartTravelFragment()
                navigate(toStartTravel)
            })
        travelViewModel.navigateToStopTravelDialog.observe(this,
            EventObserver {
                val toStopTravel: NavDirections =
                    MapsFragmentDirections.actionMapsFragmentToStopTravelFragment()
                navigate(toStopTravel)
            })

        travelViewModel.travelState.observe(
            this,
            EventObserver(::onTravelModelChange)
        )
    }

    override fun onResume() {
        super.onResume()
        mapsViewModel.requestLocationPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapsViewModel.clearMap()
        supportFragmentManager.beginTransaction().remove(mapFragment).commit()
    }

    private fun onMapsModelChange(model: MapsModel) {
        when (model) {
            is MapsModel.MapReady -> mapFragment.getMapAsync(model.onMapReady)
            is MapsModel.RequestNewLocation -> mapsViewModel.onNewLocationRequested()
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
                mapsViewModel.stopTravel()
                travelViewModel.setFabToStart()
            }
            is TravelViewModel.TravelState.OnWay -> {
                mapsViewModel.startTravel()
                travelViewModel.setFabToStop()
            }
        }
    }
}
