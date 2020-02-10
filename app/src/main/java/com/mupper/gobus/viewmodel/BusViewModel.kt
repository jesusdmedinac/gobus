package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mupper.gobus.commons.scope.ScopedViewModel
import com.mupper.domain.bus.Bus as DomainBus
import com.mupper.features.bus.AddNewBusWithTravelers
import com.mupper.gobus.commons.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
class BusViewModel(
    private val addNewBusWithTravelers: AddNewBusWithTravelers,
    uiDispatcher: CoroutineDispatcher
) : ScopedViewModel(uiDispatcher) {
    val pathName = MutableLiveData<String>()

    val pathColor = MutableLiveData<String>()
    val pathColorInt = MutableLiveData<Int>()

    val capacity = MutableLiveData<String>()

    private val _isTravelingStateToNewBus = MutableLiveData<Boolean>(false)

    private val _showColorPickerDialog = MutableLiveData<Event<Int?>>()
    val showColorPickerDialog: LiveData<Event<Int?>> get() = _showColorPickerDialog

    fun showColorPickerDialog(defaultColor: Int) {
        if (pathColorInt.value == null) {
            pathColorInt.value = defaultColor
        }
        _showColorPickerDialog.value =
            Event(pathColorInt.value)
    }

    fun onColorPicked(color: Int) {
        pathColorInt.value = color
        pathColor.value = "#${Integer.toHexString(color)}"
    }

    private fun saveNewBus() {
        launch(Dispatchers.Main) {
            val pathName = pathName.value ?: ""
            val pathColor = pathColor.value ?: ""
            val capacity = capacity.value ?: ""
            val isTraveling = _isTravelingStateToNewBus.value ?: false
            addNewBusWithTravelers.invoke(
                DomainBus(
                    pathName,
                    pathColor,
                    capacity.toInt(),
                    isTraveling
                )
            )
            _isTravelingStateToNewBus.value = false
        }
    }

    fun saveNewBusToStartTravel() {
        _isTravelingStateToNewBus.value = true
        saveNewBus()
    }
}
