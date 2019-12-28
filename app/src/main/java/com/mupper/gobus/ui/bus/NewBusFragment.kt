package com.mupper.gobus.ui.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mupper.gobus.R
import com.mupper.gobus.commons.bindingInflate
import com.mupper.gobus.commons.getViewModel
import com.mupper.gobus.commons.supportFragmentManager
import com.mupper.gobus.databinding.FragmentBusNewBinding
import com.mupper.gobus.ui.bus.stepper.NewBusStepperAdapter
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_bus_new.*

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class NewBusFragment : Fragment(), StepperLayout.StepperListener {

    private lateinit var busViewModel: BusViewModel
    private var binding: FragmentBusNewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_bus_new, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepperLayout.setAdapter(NewBusStepperAdapter(supportFragmentManager, requireActivity()))

        busViewModel =
            getViewModel { BusViewModel() }

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusFragment
        }
    }

    override fun onStepSelected(newStepPosition: Int) {
        Toast.makeText(requireActivity(), "onStepSelected", Toast.LENGTH_SHORT).show()
    }

    override fun onError(verificationError: VerificationError?) {
        Toast.makeText(requireActivity(), "onError", Toast.LENGTH_SHORT).show()
    }

    override fun onReturn() {
        Toast.makeText(requireActivity(), "onReturn", Toast.LENGTH_SHORT).show()
    }

    override fun onCompleted(completeButton: View?) {
        Toast.makeText(requireActivity(), "onCompleted", Toast.LENGTH_SHORT).show()
    }
}
