package com.mupper.gobus.ui.bus

import android.widget.SearchView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavOptions
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.mupper.gobus.DEPENDENCY_NAME_UI_DISPATCHER
import com.mupper.gobus.R
import com.mupper.gobus.utils.*
import com.mupper.gobus.viewmodel.BusViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import io.mockk.verify
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@Config(qualifiers = "normal")
@RunWith(AndroidJUnit4::class)
class NewBusFragmentTest : AutoCloseKoinTest() {

    @Rule
    @JvmField
    val dataBindingIdlingResourceRule: DataBindingIdlingResourceRule =
        DataBindingIdlingResourceRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    lateinit var fragmentScenario: FragmentScenario<NewBusFragment>

    @Before
    fun setUp() {
        val vmModule = module {
            single { BusViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            single { TravelViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)

        fragmentScenario = launchStyledFragment<NewBusFragment>()
        dataBindingIdlingResourceRule.monitorFragment(fragmentScenario)
    }

    @Test
    fun `stepperLayout should be displayed`() {
        onView(withId(R.id.stepperLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun `edtPathName should be visible`() {
        onView(withId(R.id.edtPathName)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `ms_stepNextButton click should make edtPathColor visible when edtPathName contains text`() {
        // Given
        onView(withId(R.id.edtPathName)).perform(typeTextWithoutConstraints("Bus name"))

        // When
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())

        // Then
        onView(withId(R.id.edtPathColor)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `edtPathColor click should show dialog with string Select a color`() {
        // Given
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())

        // When
        mockMeasureSpec()
        onView(withId(R.id.edtPathColor)).perform(clickWithoutConstraints())

        // Then
        val expectedString = "Select a color"
        onView(withId(R.id.alertTitle))
            .inRoot(isDialog())
            .check(matches(withText(expectedString)))
    }

    @Test
    fun `ms_stepNextButton click should make edtCapacity visible when edtPathColor contains text`() {
        // Given
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())
        mockMeasureSpec()
        onView(withId(R.id.edtPathColor)).perform(clickWithoutConstraints())
        onView(
            childAtPosition(
                childAtPosition(
                    withId(R.id.palette),
                    0
                ),
                0
            )
        ).inRoot(isDialog()).perform(clickWithoutConstraints())

        // When
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())

        // Then
        onView(withId(R.id.edtCapacity)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `ms_stepNextButton should call navigate with pop up to MapsFragment when edtCapacity is visible`() {
        // Given
        val mockNavController = findNavController()
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())
        onView(withId(R.id.ms_stepNextButton)).perform(clickWithoutConstraints())

        // When
        onView(withId(R.id.ms_stepCompleteButton)).perform(clickWithoutConstraints())

        // Then
        verify {
            val expectedActionId = NewBusFragmentDirections.actionBusNewNavToMapsFragment().actionId
            mockNavController.navigate(expectedActionId, null, any())
        }
    }
}