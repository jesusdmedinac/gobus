package com.mupper.gobus.viewmodel

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.gobus.commons.Event
import com.mupper.gobus.commons.scope.ScopedViewModel
import com.mupper.usecase.bus.AddNewBusWithTravelers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import com.mupper.domain.bus.Bus as DomainBus

/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class BusViewModel(
    private val addNewBusWithTravelers: AddNewBusWithTravelers,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {
    val pathNameLiveData = MutableLiveData<String>()

    val pathColorLiveData = MutableLiveData<String>()
    val pathColorIntLiveData = MutableLiveData<Int>()

    val capacityLiveData = MutableLiveData<String>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val isTravelingStateToNewBus = MutableLiveData(false)

    private val _showColorPickerDialog = MutableLiveData<Event<Int?>>()
    val showColorPickerDialog: LiveData<Event<Int?>> get() = _showColorPickerDialog

    fun showColorPickerDialog(defaultColor: Int) {
        if (pathColorIntLiveData.value == null) {
            pathColorIntLiveData.value = defaultColor
        }
        _showColorPickerDialog.value =
            Event(pathColorIntLiveData.value)
    }

    fun onColorPicked(@ColorInt color: Int, colorString: String) {
        pathColorIntLiveData.value = color
        pathColorLiveData.value = colorString
    }

    private fun saveNewBus() {
        launch {
            val pathName = pathNameLiveData.value ?: ""
            val pathColor = pathColorLiveData.value ?: ""
            val capacity = capacityLiveData.value ?: "0"
            val isTraveling = isTravelingStateToNewBus.value ?: false
            addNewBusWithTravelers.invoke(
                DomainBus(
                    pathName,
                    pathColor,
                    capacity.toInt(),
                    isTraveling
                )
            )
            isTravelingStateToNewBus.value = false
        }
    }

    fun saveNewBusToStartTravel() {
        isTravelingStateToNewBus.value = true
        saveNewBus()
    }
}
