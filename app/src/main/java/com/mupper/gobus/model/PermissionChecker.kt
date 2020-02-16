package com.mupper.gobus.model

import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionChecker(private val app: Application, private val permission: String) {

    fun check(): Boolean = ContextCompat.checkSelfPermission(
        app,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
