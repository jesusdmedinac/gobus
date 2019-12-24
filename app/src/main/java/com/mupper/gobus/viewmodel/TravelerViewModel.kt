package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.core.database.traveler.Traveler
import com.mupper.core.utils.LatLng
import com.mupper.gobus.repository.TravelerRepository
import kotlinx.coroutines.launch


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelerViewModel(private val travelerRepository: TravelerRepository) : ScopedViewModel() {
    private val _traveler = MutableLiveData<Traveler>()
    val traveler: LiveData<Traveler> get() = _traveler

    private val _isTraveling = MutableLiveData<Boolean>()
    val isTraveling: LiveData<Boolean>
        get() = _isTraveling

    init {
        launch {
            _traveler.value = travelerRepository.getActualTraveler()
        }
    }

    fun startTravel() {
        _isTraveling.value = true
    }

    fun stopTravel() {
        _isTraveling.value = false
    }

    fun shareActualLocation(newLocation: LatLng) {
        travelerRepository.shareActualLocation(newLocation)
    }
}