package com.mupper.gobus.ui.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.databinding.FragmentBusNewBinding
import com.mupper.gobus.ui.bus.stepper.NewBusStepperAdapter
import com.mupper.gobus.ui.bus.steps.NewBusPath
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

    companion object {
        private const val CURRENT_STEP_POSITION_KEY = "position"
    }

    private lateinit var busViewModel: BusViewModel
    private var binding: FragmentBusNewBinding? = null

    private var newBusPathSteps: List<NewBusPath> = listOf(
        NewBusPath("new_bus_path_name", "Nombre de la ruta", 0),
        NewBusPath("new_bus_path_color", "Color de la ruta", 1)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_bus_new, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stepperLayout.adapter =
            NewBusStepperAdapter(newBusPathSteps, childFragmentManager, requireActivity())
        stepperLayout.setListener(this)
        stepperLayout.currentStepPosition = 0

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
        busViewModel.checkValues()
        val toMapsFragment = NewBusFragmentDirections.actionBusNewNavToMapsFragment()
        val popUpTo = NavOptions.Builder().setPopUpTo(R.id.mapsFragment, true).build()
        view?.findNavController()?.navigate(toMapsFragment.actionId, null, popUpTo)
    }
}
