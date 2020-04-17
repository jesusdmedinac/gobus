package com.mupper.gobus

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.mupper.gobus.ui.bus.NewBusFragment
import junit.framework.TestCase.assertEquals
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule2: DataBindingIdlingResourceRule2 =
        DataBindingIdlingResourceRule2()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        launchFragmentInContainer<NewBusFragment>(null, R.style.AppTheme).let {
            dataBindingIdlingResourceRule2.monitorFragment(it)
        }
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mupper.gobus", appContext.packageName)
    }

    @Test
    fun POC() {
        Thread.sleep(1000)
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())
        Thread.sleep(1000)
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())
        Thread.sleep(1000)
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())
    }

    private fun clickWithoutConstraints() = object : ViewAction {
        override fun getDescription(): String = "Click without constraint"

        override fun getConstraints(): Matcher<View> = ViewMatchers.isClickable()

        override fun perform(uiController: UiController?, view: View?) {
            view?.performClick()
        }
    }

    @Test
    fun POC2() {
        onView(withId(R.id.ms_stepNextButton)).perform(click())
    }

    @Test
    fun POC3() {
        onView(withId(R.id.edtPathName)).check(matches(ViewMatchers.isDisplayed()))
    }
}
