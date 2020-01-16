package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler
import com.mupper.features.ShareActualLocation
import com.mupper.features.traveler.GetActualTraveler
import kotlinx.coroutines.launch


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class TravelerViewModel(
    private val shareActualLocation: ShareActualLocation
) : ScopedViewModel() {
    fun shareActualLocation(newLocation: LatLng) {
        launch {
            shareActualLocation.invoke(newLocation)
        }
    }
}