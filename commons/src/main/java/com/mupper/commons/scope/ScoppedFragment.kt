package com.mupper.commons.scope

import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineDispatcher


/**
 * Created by jesus.medina on 12/2019.
 * Mupper
 */
abstract class ScoppedFragment(uiDispatcher: CoroutineDispatcher) : Fragment(),
    Scope by Scope.Impl(uiDispatcher) {
    override fun onStart() {
        super.onStart()
        initScope()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyScope()
    }
}