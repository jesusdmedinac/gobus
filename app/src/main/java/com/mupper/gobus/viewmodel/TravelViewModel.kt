package com.mupper.gobus.viewmodel

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.commons.scope.ScopedViewModel


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class TravelViewModel(
    val playIcon: Drawable?,
    val stopIcon: Drawable?,
    val defaultFabColor: Int,
    val defaultFabIconColor: Int
) : ScopedViewModel() {
    private val _model = MutableLiveData<TravelModel>()
    val model: LiveData<TravelModel> get() = _model

    private val _fabIcon = MutableLiveData<Drawable>(playIcon)
    val fabIcon: LiveData<Drawable> get() = _fabIcon

    private val _fabColor = MutableLiveData<Int>(defaultFabColor)
    val fabColor: LiveData<Int> get() = _fabColor

    private val _fabIconColor = MutableLiveData<Int>(defaultFabIconColor)
    val fabIconColor: LiveData<Int> get() = _fabIconColor

    sealed class TravelModel {
        object StartTravel : TravelModel()
        object StopTravel : TravelModel()
    }

    fun toggleTravelState() {
        _model.value = if (model.value == TravelModel.StartTravel) {
            stopTravel()
        } else {
            startTravel()
        }
    }

    private fun startTravel(): TravelModel.StartTravel {
        _fabIcon.value = stopIcon
        _fabColor.value = defaultFabIconColor
        _fabIconColor.value = defaultFabColor
        return TravelModel.StartTravel
    }

    private fun stopTravel(): TravelModel.StopTravel {
        _fabIcon.value = playIcon
        _fabColor.value = defaultFabColor
        _fabIconColor.value = defaultFabIconColor
        return TravelModel.StopTravel
    }
}