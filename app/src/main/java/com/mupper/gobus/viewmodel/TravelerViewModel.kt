package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.domain.LatLng
import com.mupper.domain.traveler.Traveler
import com.mupper.features.ShareActualLocation
import com.mupper.features.traveler.GetCurrentTraveler
import kotlinx.coroutines.launch


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class TravelerViewModel(
    private val getCurrentTraveler: GetCurrentTraveler,
    private val shareActualLocation: ShareActualLocation
) : ScopedViewModel() {
    private val _traveler = MutableLiveData<Traveler>()
    val traveler: LiveData<Traveler> get() = _traveler

    private val _isTraveling = MutableLiveData<Boolean>()
    val isTraveling: LiveData<Boolean>
        get() = _isTraveling

    init {
        launch {
            _traveler.value = getCurrentTraveler.invoke()
        }
    }

    fun startTravel() {
        _isTraveling.value = true
    }

    fun stopTravel() {
        _isTraveling.value = false
    }

    fun shareActualLocation(newLocation: LatLng) {
        launch {
            shareActualLocation.invoke(newLocation)
        }
    }
}