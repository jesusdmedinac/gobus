package com.mupper.gobus.commons

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.commons.stepper.StepFragment.Companion.LAYOUT_RESOURCE_ID_ARG_KEY


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
fun <VDB : ViewDataBinding> newInstance(
    stepFragment: StepFragment<VDB>,
    layoutResId: Int
): StepFragment<VDB> {
    val args = Bundle()
    args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
    stepFragment.arguments = args
    return stepFragment
}