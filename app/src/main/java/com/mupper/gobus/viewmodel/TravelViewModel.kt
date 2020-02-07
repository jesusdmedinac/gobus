package com.mupper.gobus.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel
import com.mupper.gobus.commons.Event
import com.mupper.gobus.model.TravelControl
import kotlinx.coroutines.CoroutineDispatcher


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class TravelViewModel(
    private val travelControl: TravelControl,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {
    private val _navigateToStartTravelDialog = MutableLiveData<Event<Unit>>()
    val navigateToStartTravelDialog: LiveData<Event<Unit>> get() = _navigateToStartTravelDialog

    private val _navigateToStopTravelDialog = MutableLiveData<Event<Unit>>()
    val navigateToStopTravelDialog: LiveData<Event<Unit>> get() = _navigateToStopTravelDialog

    private val _navigateToBusNavigation = MutableLiveData<Event<Unit>>()
    val navigateToBusNavigation: LiveData<Event<Unit>> get() = _navigateToBusNavigation

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
            navigateToStopTravelDialog()
        } else {
            navigateToStartTravelDialog()
        }
    }

    private fun navigateToStartTravelDialog() {
        _navigateToStartTravelDialog.value = Event(Unit)
    }

    private fun navigateToStopTravelDialog() {
        _navigateToStopTravelDialog.value = Event(Unit)
    }

    fun navigateToBusNavigation() {
        _navigateToBusNavigation.value = Event(Unit)
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