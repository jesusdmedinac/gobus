package com.mupper.commons

import kotlin.reflect.KClass


/**
 * Created by jesus.medina on 01/2020.
 * Insulet Corporation
 * Andromeda
 */
/* Convenience wrapper that allows you to call getValue<Type>() instead of of getValue(Type::class) */
inline fun <reified T : Any> getValue(): T? = getValue(T::class)

/* We have no way to guarantee that an empty constructor exists, so must return T? instead of T */
fun <T : Any> getValue(clazz: KClass<T>): T? {
    clazz.constructors.forEach { con ->
        if (con.parameters.size == 0) {
            return con.call()
        }
    }
    return null
}