package com.mupper.gobus

import android.view.View
import android.widget.Button
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import org.junit.Assert.fail

fun ViewInteraction.clickWithoutConstraints() {
    perform(object : ViewAction {
        override fun getDescription(): String = "Perform click without constaints"

        override fun getConstraints(): Matcher<View> = allOf(isFocusable())

        override fun perform(uiController: UiController?, view: View?) {
            if (view == null) fail()
            val notNullButton = view as Button
            notNullButton.performClick()
        }
    })
}