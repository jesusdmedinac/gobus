package com.mupper.gobus.commons

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class EventTest {

    private lateinit var expectedContent: String
    private lateinit var stringEvent: Event<String>

    @Before
    fun setUp() {
        expectedContent = "SomeString"
        stringEvent = Event(expectedContent)
    }

    @Test
    fun `getContentIfNotHandled should return the given content`() {
        // GIVEN an instance of stringEvent

        // WHEN
        val actualContent = stringEvent.getContentIfNotHandled()

        // THEN
        assertThat(actualContent, `is`(expectedContent))
    }

    @Test
    fun `getContentIfNotHandled should return null if content was already taken`() {
        // GIVEN
        stringEvent.getContentIfNotHandled()

        // WHEN
        val actualContent = stringEvent.getContentIfNotHandled()

        // THEN
        assertThat(actualContent, `is`(nullValue()))
    }

    @Test
    fun `hasBeenHandled should return false on Event instantiation`() {
        // GIVEN an instance of stringEvent

        // WHEN
        val hasBeenHandled = stringEvent.hasBeenHandled

        // THEN
        assertThat(hasBeenHandled, `is`(false))
    }

    @Test
    fun `hasBeenHandled should return true after call getContentIfNotHandled`() {
        // GIVEN
        stringEvent.getContentIfNotHandled()

        // WHEN
        val hasBeenHandled = stringEvent.hasBeenHandled

        // THEN
        assertThat(hasBeenHandled, `is`(true))
    }

    @Test
    fun `peekContent should return the event content when hasBeenHandle is false`() {
        // GIVEN an instance of stringEvent

        // WHEN
        val hasBeenHandled = stringEvent.hasBeenHandled
        val actualContent = stringEvent.peekContent()

        // THEN
        assertThat(hasBeenHandled, `is`(false))
        assertThat(actualContent, `is`(expectedContent))
    }

    @Test
    fun `peekContent should return the event content when hasBeenHandle is true`() {
        // GIVEN
        stringEvent.getContentIfNotHandled()

        // WHEN
        val hasBeenHandled = stringEvent.hasBeenHandled
        val actualContent = stringEvent.peekContent()

        // THEN
        assertThat(hasBeenHandled, `is`(true))
        assertThat(actualContent, `is`(expectedContent))
    }
}