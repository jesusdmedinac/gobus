package com.mupper.gobus.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.mupper.gobus.R

inline fun <reified F : Fragment> launchStyledFragment() =
    launchFragment<F>(null, R.style.AppTheme)

inline fun <reified F : Fragment> launchStyledFragmentInContainer() =
    launchFragmentInContainer<F>(null, R.style.AppTheme)

inline fun <reified F : Fragment> onFragment(
    action: FragmentScenario.FragmentAction<F>
): FragmentScenario<F> {
    with(launchStyledFragment<F>()) {
        return onFragment(action)
    }
}