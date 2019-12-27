package com.mupper.gobus.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.gobus.commons.Event
import com.mupper.gobus.model.TravelControl


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelViewModel(
    val travelControl: TravelControl
) : ScopedViewModel() {
    private val _navigateToStartTravel = MutableLiveData<Event<Unit>>()
    val navigateToStartTravel: LiveData<Event<Unit>> get() = _navigateToStartTravel

    private val _navigateToStopTravel = MutableLiveData<Event<Unit>>()
    val navigateToStopTravel: LiveData<Event<Unit>> get() = _navigateToStopTravel
    
    private val _travelState = MutableLiveData<Event<TravelState>>()
    val travelState: LiveData<Event<TravelState>> get() = _travelState

    private val _fabIcon = MutableLiveData<Drawable>(travelControl.playIcon)
    val fabIcon: LiveData<Drawable> get() = _fabIcon

    private val _fabColor = MutableLiveData<Int>(travelControl.defaultFabColor)
    val fabColor: LiveData<Int> get() = _fabColor

    private val _fabIconColor = MutableLiveData<Int>(travelControl.defaultFabIconColor)
    val fabIconColor: LiveData<Int> get() = _fabIconColor

    sealed class TravelState {
        object OnWay : TravelState()
        object Walking : TravelState()
    }

    fun toggleTravelState() {
        if (travelState.value?.peekContent() == TravelState.OnWay) {
            stopTravel()
        } else {
            startTravel()
        }
    }

    private fun startTravel() {
        _navigateToStartTravel.value = Event(Unit);
    }

    private fun stopTravel() {
        _navigateToStopTravel.value = Event(Unit)
    }

    fun letsWalk() {
        _travelState.value = Event(TravelState.Walking)
    }

    fun letsTravel() {
        _travelState.value = Event(TravelState.OnWay)
    }

    fun setFabToStop() {
        with(travelControl) {
            _fabIcon.value = stopIcon
            _fabColor.value = defaultFabIconColor
            _fabIconColor.value = defaultFabColor
        }
    }

    fun setFabToStart() {
        with(travelControl) {
            _fabIcon.value = playIcon
            _fabColor.value = defaultFabColor
            _fabIconColor.value = defaultFabIconColor
        }
    }
}