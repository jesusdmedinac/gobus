package com.mupper.gobus.ui.bus.stepper

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mupper.gobus.R
import com.mupper.gobus.ui.bus.steps.NewBusPath
import com.mupper.gobus.ui.bus.steps.NewBusPathColorFragment
import com.mupper.gobus.ui.bus.steps.NewBusPathNameFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class NewBusStepperAdapter(
    private val newBusPathSteps: List<NewBusPath>,
    fm: FragmentManager,
    context: Context
) :
    AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step {
        when (position) {
            0 -> return NewBusPathNameFragment.newInstance(R.layout.fragment_bus_new_path_name)
            1 -> return NewBusPathColorFragment.newInstance(R.layout.fragment_bus_new_path_color)
            else -> throw IllegalArgumentException("Unsupported position: " + position)
        }
    }

    override fun getCount(): Int = newBusPathSteps.size

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle(newBusPathSteps[position].title)
            .create()
    }
}