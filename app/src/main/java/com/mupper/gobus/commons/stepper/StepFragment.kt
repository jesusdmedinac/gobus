package com.mupper.gobus.commons.stepper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.mupper.gobus.commons.extension.app
import com.mupper.gobus.commons.extension.bindingInflate
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.ui.bus.NewBusFragmentComponent
import com.mupper.gobus.viewmodel.BusViewModel
import com.stepstone.stepper.Step


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
abstract class StepFragment<T : ViewDataBinding> : BindingFragment<T>(), Step {

    companion object {
        const val LAYOUT_RESOURCE_ID_ARG_KEY = "messageResourceId"
    }

    override val layoutResId: Int
        get() = arguments!!.getInt(LAYOUT_RESOURCE_ID_ARG_KEY)
}

abstract class BindingFragment<T : ViewDataBinding> : Fragment() {
    private lateinit var component: NewBusFragmentComponent

    val busViewModel: BusViewModel by lazy { getViewModel { component.busViewModel } }

    var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(layoutResId, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component = app.component.newBusFragmentComponent()
    }

    @get:LayoutRes
    protected abstract val layoutResId: Int
}