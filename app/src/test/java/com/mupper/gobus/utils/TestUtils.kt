package com.mupper.gobus.utils

import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.mupper.gobus.R
import com.mupper.gobus.ui.MainActivity
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T

fun launchMainActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)

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

fun clickWithoutConstraints() = object : ViewAction {
    override fun getDescription(): String = "Click without constraint"

    override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()

    override fun perform(uiController: UiController?, view: View?) {
        view?.performClick()
    }
}

fun mockMeasureSpec() {
    // Mocking MeasureSpec that allow to calculate device screen size
    mockkStatic(MeasureSpec::class)
    every { MeasureSpec.getMode(any()) } returns -2147483648
    every { MeasureSpec.getSize(any()) } returns 2156
}

fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent)
                    && view == parent.getChildAt(position)
        }
    }
}