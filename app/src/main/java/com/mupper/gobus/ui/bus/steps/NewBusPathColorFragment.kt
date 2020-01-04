package com.mupper.gobus.ui.bus.steps

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.EventObserver
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.databinding.FragmentBusNewPathColorBinding
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.VerificationError
import com.thebluealliance.spectrum.SpectrumDialog

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class NewBusPathColorFragment : StepFragment<FragmentBusNewPathColorBinding>() {
    companion object {
        fun newInstance(@LayoutRes layoutResId: Int): NewBusPathColorFragment {
            val args = Bundle()
            args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
            val fragment = NewBusPathColorFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busViewModel =
            getViewModel { BusViewModel() }

        busViewModel.showColorPickerDialog.observe(this, EventObserver { color ->
            showColorPicker(color)
        })

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@NewBusPathColorFragment
        }
    }

    private fun showColorPicker(color: Int?) {
        val colorPicker = SpectrumDialog.Builder(requireActivity())
        colorPicker.setColors(R.array.md_colors)
        color?.let { colorPicker.setSelectedColor(it) }
        colorPicker.setOnColorSelectedListener { positiveResult, colorInt ->
            busViewModel.onColorPicked(colorInt)
        }
        colorPicker.build().show(childFragmentManager, "¿De qué color es el camión?")
    }

    override fun onSelected() {
    }

    override fun verifyStep(): VerificationError? = null

    override fun onError(error: VerificationError) {
    }
}
