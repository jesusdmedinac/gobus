package com.mupper.gobus.ui.bus.steps


import android.os.Bundle
import android.view.View
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.app
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.commons.newInstance
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.databinding.FragmentBusNewCapacityBinding
import com.mupper.gobus.repository.BusRepository
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.VerificationError

/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
class NewBusCapacityFragment : StepFragment<FragmentBusNewCapacityBinding>() {
    companion object {
        fun newInstance(): StepFragment<FragmentBusNewCapacityBinding> = newInstance(
            NewBusCapacityFragment(),
            R.layout.fragment_bus_new_capacity
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busViewModel =
            getViewModel { BusViewModel(BusRepository(app, TravelerRepository(app))) }

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusCapacityFragment
        }
    }

    override fun onSelected() {}

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) {}
}
