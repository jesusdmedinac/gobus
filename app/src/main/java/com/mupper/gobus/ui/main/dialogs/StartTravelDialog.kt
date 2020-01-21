package com.mupper.gobus.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mupper.gobus.R
import com.mupper.gobus.commons.EventObserver
import com.mupper.gobus.commons.extension.app
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.commons.extension.navigate
import com.mupper.gobus.ui.main.MapsFragmentComponent
import com.mupper.gobus.viewmodel.TravelViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class StartTravelDialog : DialogFragment() {

    private lateinit var component: MapsFragmentComponent

    private val travelViewModel: TravelViewModel by lazy { getViewModel { component.travelViewModel }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component = app.component.mapsFragmentComponent()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.lets_travel_title)
            .setMessage(R.string.lets_travel_message)
            .setPositiveButton(R.string.lets_travel) { _, _ ->
                travelViewModel.navigateToBusNavigation()
            }
            .setNegativeButton(R.string.maybe_later) { _, _ ->
                dismiss()
            }
            .setCancelable(true)
            .create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        travelViewModel.navigateToBusNavigation.observe(this,
            EventObserver {
                val toBusNavigation: NavDirections =
                    StartTravelDialogDirections.actionStartTravelFragmentToBusNav()
                navigate(toBusNavigation)
            })

        return view
    }
}