package com.mupper.gobus.commons

import android.os.Bundle
import com.mupper.gobus.R
import com.mupper.gobus.ui.bus.steps.NewBusCapacityFragment
import com.mupper.gobus.ui.bus.steps.NewBusPathColorFragment
import com.mupper.gobus.ui.bus.steps.NewBusPathNameFragment
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GenericsKtTest {
    @Spy
    lateinit var args: Bundle

    @Before
    fun setUp() {
        willDoNothing().given(args).putInt(any(), any())
    }

    @Test
    fun `newStepInstance should return a new instance of expected NewBusCapacityFragment`() {
        // GIVEN
        val expectedNewBusCapacityFragment: NewBusCapacityFragment = mock()

        // WHEN
        val newBusCapacityFragment = newStepInstance(
            expectedNewBusCapacityFragment,
            R.layout.fragment_bus_new_capacity,
            args
        )

        // THEN
        assertThat(newBusCapacityFragment, instanceOf(NewBusCapacityFragment::class.java))
    }

    @Test
    fun `newStepInstance should return a new instance of expected NewBusPathColorFragment`() {
        // GIVEN
        val expectedNewBusPathColorFragment: NewBusPathColorFragment = mock()

        // WHEN
        val newBusPathColorFragment = newStepInstance(
            expectedNewBusPathColorFragment,
            R.layout.fragment_bus_new_path_color,
            args
        )

        // THEN
        assertThat(newBusPathColorFragment, instanceOf(NewBusPathColorFragment::class.java))
    }

    @Test
    fun `newStepInstance should return a new instance of expected NewBusPathNameFragment`() {
        // GIVEN
        val expectedNewBusPathNameFragment: NewBusPathNameFragment = mock()

        // WHEN
        val newBusPathNameFragment = newStepInstance(
            expectedNewBusPathNameFragment,
            R.layout.fragment_bus_new_path_color,
            args
        )

        // THEN
        assertThat(newBusPathNameFragment, instanceOf(NewBusPathNameFragment::class.java))
    }

    @Test
    fun `newInstance should return an instance of expected object`() {
        // GIVEN SomeClassToTest

        // WHEN
        val someClassToTest = newInstance<SomeClassToTest>()

        // THEN
        assertThat(someClassToTest, instanceOf(SomeClassToTest::class.java))
    }

    @Test(expected = Exception::class)
    fun `newInstance should return null when the class has no accessible constructor`() {
        // GIVEN SomeClassToTestWithPrivateConstructor

        // WHEN
        newInstance<SomeClassToTestWithPrivateConstructor>()

        // THEN throw some exception
    }

    class SomeClassToTest

    class SomeClassToTestWithPrivateConstructor private constructor()
}
