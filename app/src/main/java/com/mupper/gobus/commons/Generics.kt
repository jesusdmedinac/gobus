package com.mupper.gobus.commons

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.commons.stepper.StepFragment.Companion.LAYOUT_RESOURCE_ID_ARG_KEY
import kotlin.reflect.KClass


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
fun <VDB : ViewDataBinding> newStepInstance(
    stepFragment: StepFragment<VDB>,
    @LayoutRes layoutResId: Int,
    args: Bundle
): StepFragment<VDB> = stepFragment.apply {
    arguments = args.apply {
        putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
    }
}

/* Convenience wrapper that allows you to call getValue<Type>() instead of of getValue(Type::class) */
inline fun <reified T : Any> newInstance(): T? = newInstance(T::class)

/* We have no way to guarantee that an empty constructor exists, so must return T? instead of T */
fun <T : Any> newInstance(clazz: KClass<T>): T? {
    clazz.constructors.first { con ->
        return con.call()
    }
    return null
}
