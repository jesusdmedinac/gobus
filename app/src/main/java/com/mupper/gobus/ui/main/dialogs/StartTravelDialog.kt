package com.mupper.gobus.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.EventObserver
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.commons.extension.navigate
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.viewmodel.TravelViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class StartTravelDialog : DialogFragment() {

    private lateinit var dialog: AlertDialog
    private lateinit var travelViewModel: TravelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        travelViewModel =
            getViewModel {
                TravelViewModel(
                    TravelControl(requireContext())
                )
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.lets_travel_title)
            .setMessage(R.string.lets_travel_message)
            .setPositiveButton(R.string.lets_travel) { _, _ ->
                travelViewModel.letsTravel()
            }
            .setNegativeButton(R.string.maybe_later) { _, _ ->
                dismiss()
            }
            .setCancelable(true)

        dialog = builder.create()
        return dialog
    }

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