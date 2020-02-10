package com.mupper.gobus.ui.bus.steps

import android.os.Bundle
import android.view.View
import com.mupper.gobus.R
import com.mupper.gobus.commons.newInstance
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.databinding.FragmentBusNewPathNameBinding
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

    override fun onSelected() {
        // TODO: Animation
    }

    override fun verifyStep(): VerificationError? {
        // TODO: Handle error
        return null
    }

    override fun onError(error: VerificationError) {
        // TODO: Handle error
    }
}
