package com.mupper.gobus.ui.bus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mupper.gobus.R
import com.mupper.gobus.commons.bindingInflate
import com.mupper.gobus.commons.getViewModel
import com.mupper.gobus.databinding.FragmentBusBinding
import com.mupper.gobus.viewmodel.BusViewModel

/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class BusFragment : Fragment() {

    private lateinit var busViewModel: BusViewModel
    private var binding: FragmentBusBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_bus, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busViewModel =
            getViewModel { BusViewModel() }

        binding?.apply {
            bus = busViewModel
            lifecycleOwner = this@BusFragment
        }
    }
}
