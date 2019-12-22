package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mupper.core.database.traveler.Traveler
import com.mupper.gobus.scope.Scope
import com.mupper.core.utils.LatLng
import com.mupper.gobus.repository.TravelerRepository
import com.mupper.gobus.scope.ScopedViewModel
import kotlinx.coroutines.launch


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelerViewModel(private val travelerRepository: TravelerRepository): ScopedViewModel() {
    private val _traveler = MutableLiveData<Traveler>()
    val traveler: LiveData<Traveler> get() = _traveler

    init {
        launch {
            _traveler.value = travelerRepository.getActualTraveler()
            updateUi()
        }
    }

    fun shareActualLocation(newLocation: LatLng) {
        travelerRepository.shareActualLocation(newLocation)
    }

    private fun updateUi() {
        // TODO("not implemented") //Add update of new live data
    }
}