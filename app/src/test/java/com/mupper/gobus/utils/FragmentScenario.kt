package com.mupper.gobus.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.mupper.gobus.R

inline fun <reified F : Fragment> launchStyledFragment(
    factory: FragmentFactory? = null
) =
    launchFragmentInContainer<F>(null, R.style.AppTheme, factory)


inline fun <reified F : Fragment> launchStyledFragment(
    crossinline instantiate: () -> F
) =
    launchFragmentInContainer(null, R.style.AppTheme, instantiate)

inline fun <reified F : Fragment> onFragment(
    action: FragmentScenario.FragmentAction<F>,
    crossinline instantiate: () -> F
): FragmentScenario<F> {
    with(launchStyledFragment(instantiate)) {
        return onFragment(action)
    }
}

inline fun <reified F : Fragment> onFragment(
    action: FragmentScenario.FragmentAction<F>
): FragmentScenario<F> {
    with(launchStyledFragment<F>(null)) {
        return onFragment(action)
    }
}