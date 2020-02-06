package com.mupper.gobus.commons.scope

import androidx.appcompat.app.AppCompatActivity
import com.mupper.gobus.commons.scope.Scope


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
abstract class ScoppedActivity : AppCompatActivity(), Scope by Scope.Impl() {
    override fun onStart() {
        super.onStart()
        initScope()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyScope()
    }
}