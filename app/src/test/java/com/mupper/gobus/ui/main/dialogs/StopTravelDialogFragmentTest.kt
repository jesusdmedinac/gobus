package com.mupper.gobus.ui.main.dialogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario.FragmentAction
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mupper.gobus.*
import com.mupper.gobus.utils.*
import com.mupper.gobus.viewmodel.MapViewModel
import com.mupper.gobus.viewmodel.TravelViewModel
import com.mupper.gobus.viewmodel.TravelerViewModel
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
class StopTravelDialogFragmentTest : AutoCloseKoinTest() {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val vmModule = module {
            single { MapViewModel(get(), get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            single { TravelerViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
            single { TravelViewModel(get(), get(named(DEPENDENCY_NAME_UI_DISPATCHER))) }
        }

        initMockedDi(vmModule)
    }

    @Test
    fun `dialog should be showing`() {
        // GIVEN
        // WHEN
        onFragment(FragmentAction<StopTravelDialogFragment> { fragment ->
            // THEN
            assertThat(fragment.requireDialog().isShowing, `is`(true))
        })
    }

    @Test
    fun `title text should be from string resources lets_travel_title`() {
        // GIVEN
        // WHEN
        onFragment(FragmentAction<StopTravelDialogFragment> {
            val expectedString = app.getString(R.string.stop_travel_title)

            // THEN
            onView(withId(R.id.alertTitle))
                .inRoot(isDialog())
                .check(matches(withText(expectedString)))
        })
    }

    @Test
    fun `positive button text should be from string resources lets_travel`() {
        // GIVEN
        // WHEN
        onFragment(FragmentAction<StopTravelDialogFragment> {
            val expectedString = app.getString(R.string.stop)

            // THEN
            onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .check(matches(withText(expectedString)))
        })
    }

    @Test
    fun `negative button text should be from string resources maybe_later`() {
        // GIVEN
        // WHEN
        onFragment(FragmentAction<StopTravelDialogFragment> {
            val expectedString = app.getString(R.string.keep_travel)

            // THEN
            onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .check(matches(withText(expectedString)))
        })
    }

    @Test
    fun `positive button click should dismiss dialog`() {
        // GIVEN
        findNavController()
        onFragment(FragmentAction<StopTravelDialogFragment> { fragment ->
            val requiredDialog = fragment.requireDialog()
            assertThat(requiredDialog.isShowing, `is`(true))

            // WHEN
            onView(withId(android.R.id.button1))
                .inRoot(isDialog())
                .clickWithoutConstraints()

            // THEN
            assertThat(requiredDialog.isShowing, `is`(false))
        })
    }

    @Test
    fun `negative button click should dismiss dialog`() {
        // GIVEN
        findNavController()
        onFragment(FragmentAction<StopTravelDialogFragment> { fragment ->
            val requiredDialog = fragment.requireDialog()
            assertThat(requiredDialog.isShowing, `is`(true))

            // WHEN
            onView(withId(android.R.id.button2))
                .inRoot(isDialog())
                .clickWithoutConstraints()

            // THEN
            assertThat(requiredDialog.isShowing, `is`(false))
        })
    }
}