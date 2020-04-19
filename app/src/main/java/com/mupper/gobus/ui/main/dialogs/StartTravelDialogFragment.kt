package com.mupper.gobus.ui.main.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mupper.gobus.R
import com.mupper.gobus.commons.EventObserver
import com.mupper.gobus.commons.extension.navigateTo
import com.mupper.gobus.viewmodel.TravelViewModel
import org.koin.android.ext.android.inject

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class StartTravelDialogFragment : DialogFragment() {

    private val travelViewModel: TravelViewModel by inject()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
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

    override fun onResume() {
        super.onResume()

        travelViewModel.navigateToBusNavigationLiveData.observe(
            this,
            EventObserver {
                val toBusNavigation: NavDirections =
                    StartTravelDialogFragmentDirections.actionStartTravelDialogFragmentToBusNav()
                navigateTo(toBusNavigation)
            })
    }
}
