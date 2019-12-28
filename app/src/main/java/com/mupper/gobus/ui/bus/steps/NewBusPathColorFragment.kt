package com.mupper.gobus.ui.bus.steps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mupper.gobus.R
import com.mupper.gobus.commons.bindingInflate
import com.mupper.gobus.commons.getViewModel
import com.mupper.gobus.databinding.FragmentBusNewPathColorBinding
import com.mupper.gobus.databinding.FragmentBusNewPathNameBinding
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class NewBusPathColorFragment : Fragment(), Step {

    private lateinit var busViewModel: BusViewModel
    private var binding: FragmentBusNewPathColorBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_bus_new_path_color, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busViewModel =
            getViewModel { BusViewModel() }

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusPathColorFragment
        }
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? = VerificationError("¿De qué color es la ruta?")

    override fun onError(error: VerificationError) {
    }
}
