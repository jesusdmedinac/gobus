package com.mupper.gobus.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mupper.gobus.R
import com.mupper.gobus.commons.app
import com.mupper.gobus.commons.getCompatColor
import com.mupper.gobus.commons.getCompatDrawable
import com.mupper.gobus.commons.getViewModel
import com.mupper.gobus.model.TravelControl
import com.mupper.gobus.repository.LocationRepository
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.viewmodel.MapsViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class StopTravelDialog : DialogFragment() {

    private lateinit var travelViewModel: TravelViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
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

        return dialog.create()
    }

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