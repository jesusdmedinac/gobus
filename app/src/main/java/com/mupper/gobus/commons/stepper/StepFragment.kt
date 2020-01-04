package com.mupper.gobus.commons.stepper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.Step


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
abstract class StepFragment<T : ViewDataBinding> : BindingFragment<T>(), Step {

    companion object {
        val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"
    }

    override val layoutResId: Int
        get() = arguments!!.getInt(LAYOUT_RESOURCE_ID_ARG_KEY)
}

abstract class BindingFragment<T : ViewDataBinding> : Fragment() {
    lateinit var busViewModel: BusViewModel
    var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(layoutResId, false)
        return binding?.root
    }

    @get:LayoutRes
    protected abstract val layoutResId: Int
}