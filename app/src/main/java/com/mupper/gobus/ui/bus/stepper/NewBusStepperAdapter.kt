package com.mupper.gobus.ui.bus.stepper

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.mupper.gobus.ui.bus.steps.NewBusPathNameFragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class NewBusStepperAdapter(fm: FragmentManager, context: Context) :
    AbstractFragmentStepAdapter(fm, context) {

    val busSteps: List<Triple<String, String, Int>> = arrayListOf(
        Triple("new_bus_path_name", "Nombre de la ruta", 0),
        Triple("new_bus_path_color", "Color de la ruta", 1)
    )

    override fun createStep(position: Int): Step {
        val newBusPathNameFragment =
            NewBusPathNameFragment()
        val b = Bundle()

        for (bus in busSteps) {
            b.putInt(bus.first, bus.third)
        }

        newBusPathNameFragment.arguments = b
        return newBusPathNameFragment
    }

    override fun getCount(): Int = busSteps.size

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(context)
            .setTitle(busSteps[position].second)
            .create()
    }
}