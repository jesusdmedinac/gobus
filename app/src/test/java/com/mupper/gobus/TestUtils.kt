package com.mupper.gobus

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import androidx.fragment.app.testing.launchFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.mupper.gobus.ui.MainActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T

fun launchMainActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)

inline fun <reified F : Fragment> onFragment(action: FragmentAction<F>): FragmentScenario<F> {
    with(launchFragment<F>(null, R.style.AppTheme)) {
        return onFragment(action)
    }
}

fun findNavController(): NavController {
    mockkStatic(Navigation::class)
    mockkStatic(NavHostFragment::class)
    val navController: NavController = mockk(relaxed = true)
    every {
        Navigation.findNavController(any(), any())
    } returns navController
    every {
        NavHostFragment.findNavController(any())
    } returns navController
    return navController
}
