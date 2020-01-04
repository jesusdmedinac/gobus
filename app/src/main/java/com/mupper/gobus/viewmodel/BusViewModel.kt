package com.mupper.gobus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mupper.commons.d
import com.mupper.gobus.commons.extension.Event


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
class BusViewModel : ViewModel() {
    val pathName = MutableLiveData<String>()

    val pathColor = MutableLiveData<String>()
    val pathColorInt = MutableLiveData<Int>()

    private val _showColorPickerDialog = MutableLiveData<Event<Int?>>()
    val showColorPickerDialog: LiveData<Event<Int?>> get() = _showColorPickerDialog

    fun showColorPickerDialog(defaultColor: Int) {
        if (pathColorInt.value == null) {
            pathColorInt.value = defaultColor
        }
        _showColorPickerDialog.value = Event(pathColorInt.value)
    }

    fun onColorPicked(color: Int) {
        pathColorInt.value = color
        pathColor.value = "#${Integer.toHexString(color)}"
    }

    fun checkValues() {
        d("path name: ${pathName.value}")
        d("path name: ${pathColor.value}")
    }
}