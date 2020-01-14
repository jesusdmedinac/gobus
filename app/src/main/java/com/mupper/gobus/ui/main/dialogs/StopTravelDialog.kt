package com.mupper.gobus.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mupper.gobus.R
import com.mupper.gobus.commons.extension.getViewModel
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.viewmodel.TravelViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class StopTravelDialog : DialogFragment() {

    private lateinit var travelViewModel: TravelViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.stop_travel_title)
            .setMessage(R.string.stop_travel_message)
            .setPositiveButton(R.string.stop) { _, _ ->
                dismiss()
                travelViewModel.letsWalk()
            }
            .setNegativeButton(R.string.keep_travel) { _, _ ->
                dismiss()
            }
            .setCancelable(true)
            .create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        travelViewModel =
            getViewModel {
                TravelViewModel(
                    TravelControl(requireContext())
                )
            }
    }
}