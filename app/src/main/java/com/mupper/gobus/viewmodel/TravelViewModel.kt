package com.mupper.gobus.viewmodel

import android.graphics.drawable.Drawable
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.data.source.resources.TravelControlDataSource
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.scope.ScopedViewModel
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class TravelViewModel(
    private val travelControl: TravelControlDataSource<Drawable>,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {
    private val _navigateToStartTravelDialogLiveData = MutableLiveData<Event<Unit>>()
    val navigateToStartTravelDialogLiveData: LiveData<Event<Unit>> get() = _navigateToStartTravelDialogLiveData

    private val _navigateToStopTravelDialogLiveData = MutableLiveData<Event<Unit>>()
    val navigateToStopTravelDialogLiveData: LiveData<Event<Unit>> get() = _navigateToStopTravelDialogLiveData

    private val _navigateToBusNavigationLiveData = MutableLiveData<Event<Unit>>()
    val navigateToBusNavigationLiveData: LiveData<Event<Unit>> get() = _navigateToBusNavigationLiveData

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val travelStateMutableLiveData = MutableLiveData<Event<TravelState>>()
    val travelStateLiveData: LiveData<Event<TravelState>> get() = travelStateMutableLiveData

    private val _fabIconLiveData = MutableLiveData(travelControl.playIcon)
    val fabIconLiveData: LiveData<Drawable> get() = _fabIconLiveData

    private val _fabColorLiveData = MutableLiveData(travelControl.defaultFabColor)
    val fabColorLiveData: LiveData<Int> get() = _fabColorLiveData

    private val _fabIconColorLiveData = MutableLiveData(travelControl.defaultFabIconColor)
    val fabIconColorLiveData: LiveData<Int> get() = _fabIconColorLiveData

    sealed class TravelState {
        object OnWay : TravelState()
        object Walking : TravelState()
    }

    fun toggleTravelState() {
        if (travelStateLiveData.value?.peekContent() == TravelState.OnWay) {
            navigateToStopTravelDialog()
        } else {
            navigateToStartTravelDialog()
        }
    }

    private fun navigateToStartTravelDialog() {
        _navigateToStartTravelDialogLiveData.value = Event(Unit)
    }

    private fun navigateToStopTravelDialog() {
        _navigateToStopTravelDialogLiveData.value = Event(Unit)
    }

    fun navigateToBusNavigation() {
        _navigateToBusNavigationLiveData.value = Event(Unit)
    }

    fun letsWalk() {
        travelStateMutableLiveData.value = Event(TravelState.Walking)
    }

    fun letsTravel() {
        travelStateMutableLiveData.value = Event(TravelState.OnWay)
    }

    fun setFabToStop() {
        with(travelControl) {
            _fabIconLiveData.value = stopIcon
            _fabColorLiveData.value = defaultFabIconColor
            _fabIconColorLiveData.value = defaultFabColor
        }
    }

    fun setFabToStart() {
        with(travelControl) {
            _fabIconLiveData.value = playIcon
            _fabColorLiveData.value = defaultFabColor
            _fabIconColorLiveData.value = defaultFabIconColor
        }
    }
}
