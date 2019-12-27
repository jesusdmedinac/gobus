package com.mupper.gobus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import com.mupper.gobus.commons.*
import com.mupper.gobus.databinding.ActivityMainBinding
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.ui.main.MapsFragmentDirections
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import kotlinx.android.synthetic.main.activity_main.btnToggleTravel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var travelViewModel: TravelViewModel
    private lateinit var travelerViewModel: TravelerViewModel

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = bindingInflate(R.layout.activity_main)

        val playIcon = getCompatDrawable(R.drawable.ic_start)
        val stopIcon = getCompatDrawable(R.drawable.ic_stop)
        val defaultFabColor = getCompatColor(R.color.colorAccent)
        val defaultFabIconColor = getCompatColor(R.color.white)

        mapsViewModel =
            getViewModel { MapsViewModel(LocationRepository(app)) }
        travelerViewModel =
            getViewModel { TravelerViewModel(TravelerRepository(app)) }
        travelViewModel =
            getViewModel {
                TravelViewModel(
                    playIcon,
                    stopIcon,
                    defaultFabColor,
                    defaultFabIconColor
                )
            }

        binding?.apply {
            travel = travelViewModel
            lifecycleOwner = this@MainActivity
        }

        travelViewModel.navigateToStartTravel.observe(this, EventObserver {
            val toStartTravel: NavDirections =
                MapsFragmentDirections.actionMapsFragmentToStartTravelFragment()
            navigate(toStartTravel)
        })
        travelViewModel.navigateToStopTravel.observe(this, EventObserver {
            val toStopTravel: NavDirections =
                MapsFragmentDirections.actionMapsFragmentToStopTravelFragment()
            navigate(toStopTravel)
        })

        travelViewModel.travelState.observe(this, EventObserver(::onTravelModelChange))
    }

    private fun onTravelModelChange(state: TravelViewModel.TravelState) {
        when (state) {
            is TravelViewModel.TravelState.Walking -> {
                mapsViewModel.stopTravel()
                travelerViewModel.stopTravel()
                travelViewModel.setFabToStart()
            }
            is TravelViewModel.TravelState.OnWay -> {
                mapsViewModel.startTravel()
                travelerViewModel.startTravel()
                travelViewModel.setFabToStop()
            }
        }
    }
}