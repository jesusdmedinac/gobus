package com.mupper.gobus.commons.scope

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.mupper.gobus.commons.scope.Scope

abstract class ScopedViewModel : ViewModel(), Scope by Scope.Impl() {

    init {
        initScope()
    }

    @CallSuper
    override fun onCleared() {
        destroyScope()
        super.onCleared()
    }
}