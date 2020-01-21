package com.mupper.gobus.ui.bus.steps

import android.os.Bundle
import android.view.View
import com.mupper.features.ShareActualLocation
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.features.bus.GetTravelingBus
import com.mupper.features.traveler.GetActualTraveler
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.app
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.commons.newInstance
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.data.source.traveler.TravelerRoomDataSource
import com.mupper.gobus.data.source.bus.BusFirebaseDataSource
import com.mupper.gobus.data.source.bus.BusRoomDataSource
import com.mupper.gobus.data.source.traveler.TravelerFirebaseDataSource
import com.mupper.gobus.databinding.FragmentBusNewPathNameBinding
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.VerificationError

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class NewBusPathNameFragment : StepFragment<FragmentBusNewPathNameBinding>() {
    companion object {
        fun newInstance(): StepFragment<FragmentBusNewPathNameBinding> {
            return newInstance(NewBusPathNameFragment(), R.layout.fragment_bus_new_path_name)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusPathNameFragment
        }
    }

    override fun onSelected() {}

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) {}
}
