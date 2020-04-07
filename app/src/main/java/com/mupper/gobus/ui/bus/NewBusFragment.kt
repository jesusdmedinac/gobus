package com.mupper.gobus.ui.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.databinding.FragmentNewBusBinding
import com.mupper.gobus.ui.bus.stepper.NewBusStepperAdapter
import com.mupper.gobus.ui.bus.steps.NewBusStep
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_new_bus.*
import org.koin.android.ext.android.inject

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class NewBusFragment : Fragment(), StepperLayout.StepperListener {

    private val busViewModel: BusViewModel by inject()
    private val travelViewModel: TravelViewModel by inject()

    private var binding: FragmentNewBusBinding? = null

    private var newBusStepSteps: List<NewBusStep> = listOf(
        NewBusStep("new_bus_path_name", "Nombre de la ruta", 0),
        NewBusStep("new_bus_path_color", "Color de la ruta", 1),
        NewBusStep("new_bus_capacity", "Capcidad del autobus", 2)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_new_bus, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepperLayout.adapter =
            NewBusStepperAdapter(newBusStepSteps, childFragmentManager, requireActivity())
        stepperLayout.setListener(this)
        stepperLayout.currentStepPosition = 0

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusFragment
        }
    }

    override fun onStepSelected(newStepPosition: Int) {
        // TOD O: Animation
    }

    override fun onError(verificationError: VerificationError?) {
        // TOD O: Handle error
    }

    override fun onReturn() {
        // TOD O: Animation
    }

    override fun onCompleted(completeButton: View?) {
        busViewModel.saveNewBusToStartTravel()
        travelViewModel.letsTravel()
        val toMapsFragment = NewBusFragmentDirections.actionBusNewNavToMapsFragment()
        val popUpTo = NavOptions.Builder().setPopUpTo(R.id.mapsFragment, true).build()
        view?.findNavController()?.navigate(toMapsFragment.actionId, null, popUpTo)
    }
}
