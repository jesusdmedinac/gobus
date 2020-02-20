package com.mupper.gobus.commons

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Before
import org.junit.Test

class EventObserverTest {

    private lateinit var expectedContent: String
    private lateinit var stringEvent: Event<String>

    @Before
    fun setUp() {
        expectedContent = "SomeString"
        stringEvent = spy(Event(expectedContent))
    }

    @Test
    fun `onChanged should call getContentIfNotHandle`() {
        // GIVEN
        val eventObserver = EventObserver<String> { }
        given(stringEvent.getContentIfNotHandled()).willReturn(expectedContent)

        // WHEN
        eventObserver.onChanged(stringEvent)

        // THEN
        verify(stringEvent).getContentIfNotHandled()
    }

    @Test
    fun `onChanged should not call getContentIfNotHandle when event is null`() {
        // GIVEN
        val eventObserver = EventObserver<String> { }

        // WHEN
        eventObserver.onChanged(null)

        // THEN
        verifyZeroInteractions(stringEvent)
    }

    class ClassThatHasOnEventUnhandledContentFunction {
        fun expectedOnEventUnhandledContent(string: String) = Unit
    }

    @Test
    fun `onChanged should call onEventUnhandledContent when getContentIfNotHandled returns expected string content`() {
        // GIVEN
        val classThatHasOnEventUnhandledContentFunction: ClassThatHasOnEventUnhandledContentFunction = spy()

        val eventObserver = EventObserver(classThatHasOnEventUnhandledContentFunction::expectedOnEventUnhandledContent)
        given(stringEvent.getContentIfNotHandled()).willReturn(expectedContent)

        // WHEN
        eventObserver.onChanged(stringEvent)

        // THEN
        verify(classThatHasOnEventUnhandledContentFunction).expectedOnEventUnhandledContent(expectedContent)
    }
}