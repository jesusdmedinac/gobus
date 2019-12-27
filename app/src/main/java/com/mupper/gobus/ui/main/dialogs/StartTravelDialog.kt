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
class StartTravelDialog : DialogFragment() {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var travelViewModel: TravelViewModel
    private lateinit var travelerViewModel: TravelerViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.lets_travel_title)
            .setMessage(R.string.lets_travel_message)
            .setPositiveButton(R.string.lets_travel) {_, _ ->
                travelViewModel.letsTravel()
            }
            .setNegativeButton(R.string.maybe_later) {_, _ ->
                dismiss()
            }
            .setCancelable(true)

        return dialog.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val playIcon = getCompatDrawable(R.drawable.ic_start)
        val stopIcon = getCompatDrawable(R.drawable.ic_stop)
        val defaultFabColor = getCompatColor(R.color.colorAccent)
        val defaultFabIconColor = getCompatColor(R.color.white)

        mapsViewModel =
            getViewModel { MapsViewModel(LocationRepository(app)) }
        travelerViewModel =
            getViewModel { TravelerViewModel(TravelerRepository(app)) }
        travelViewModel =
            getViewModel {
                TravelViewModel(
                    playIcon,
                    stopIcon,
                    defaultFabColor,
                    defaultFabIconColor
                )
            }
    }
}