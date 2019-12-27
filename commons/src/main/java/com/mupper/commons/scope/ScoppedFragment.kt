package com.mupper.commons.scope

import androidx.fragment.app.Fragment


/**
 * Created by jesus.medina on 12/2019.
 * Insulet Corporation
 * Andromeda
 */
abstract class ScoppedFragment : Fragment(), Scope by Scope.Impl() {
    override fun onStart() {
        super.onStart()
        initScope()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyScope()
    }
}