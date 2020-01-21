package com.mupper.gobus.ui.bus.stepper

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.mupper.gobus.ui.bus.steps.NewBusStep
import com.mupper.gobus.ui.bus.steps.NewBusCapacityFragment
import com.mupper.gobus.ui.bus.steps.NewBusPathColorFragment
import com.mupper.gobus.ui.bus.steps.NewBusPathNameFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class NewBusStepperAdapter(
    private val newBusStepSteps: List<NewBusStep>,
    fm: FragmentManager,
    context: Context
) :
    AbstractFragmentStepAdapter(fm, context) {

    override fun createStep(position: Int): Step {
        return when (position) {
            0 -> NewBusPathNameFragment.newInstance()
            1 -> NewBusPathColorFragment.newInstance()
            2 -> NewBusCapacityFragment.newInstance()
            else -> throw IllegalArgumentException("Unsupported position: " + position)
        }
    }

    override fun getCount(): Int = newBusStepSteps.size

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle(newBusStepSteps[position].title)
            .create()
    }
}