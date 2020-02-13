package com.mupper.gobus.commons

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.mupper.gobus.commons.stepper.StepFragment
import com.mupper.gobus.commons.stepper.StepFragment.Companion.LAYOUT_RESOURCE_ID_ARG_KEY
import kotlin.reflect.KClass


/**
 * Created by jesus.medina on 01/2020.
 * Mupper
 */
fun <VDB : ViewDataBinding> newInstance(
    stepFragment: StepFragment<VDB>,
    layoutResId: Int
): StepFragment<VDB> {
    val args = Bundle()
    args.putInt(LAYOUT_RESOURCE_ID_ARG_KEY, layoutResId)
    stepFragment.arguments = args
    return stepFragment
}

/* Convenience wrapper that allows you to call getValue<Type>() instead of of getValue(Type::class) */
inline fun <reified T : Any> getValue(): T? = getValue(T::class)

/* We have no way to guarantee that an empty constructor exists, so must return T? instead of T */
fun <T : Any> getValue(clazz: KClass<T>): T? {
    clazz.constructors.forEach { con ->
        return con.call()
    }
    return null
}
