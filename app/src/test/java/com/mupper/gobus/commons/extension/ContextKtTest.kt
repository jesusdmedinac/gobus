package com.mupper.gobus.commons.extension

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.mupper.gobus.GobusApp
import com.mupper.gobus.R
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.BDDMockito.willDoNothing
import org.mockito.BDDMockito.willReturn

@Suppress("DEPRECATION")
class ContextKtTest {
    @Test
    fun `app should return applicationContext as an instance of GobusApp`() {
        // GIVEN
        val mockContext: Context = mock()
        val applicationContext: GobusApp = mock()
        willReturn(applicationContext).given(mockContext).applicationContext

        // WHEN
        val expectedApp = mockContext.app

        // THEN
        assertThat(expectedApp, instanceOf(GobusApp::class.java))
    }

    @Test
    fun `getCompatDrawable should return an instance of Drawable`() {
        // GIVEN
        // mock resources
        val mockResources: Resources = mock()
        willDoNothing().given(mockResources).getValue(any<Int>(), any(), any())
        val mockDrawable: Drawable = mock()
        willReturn(mockDrawable).given(mockResources).getDrawable(any())
        // mock context
        val mockContext: Context = mock()
        willReturn(mockResources).given(mockContext).resources

        // WHEN
        val expectedDrawable = mockContext.getCompatDrawable(R.drawable.ic_bus_marker)

        // THEN
        assertThat(expectedDrawable, instanceOf(Drawable::class.java))
    }

    @Test
    fun `getCompatColor should return an instance of Int color`() {
        // GIVEN
        // mock resources
        val mockResources: Resources = mock()
        willDoNothing().given(mockResources).getValue(any<Int>(), any(), any())
        val colorId = R.color.md_red_500
        willReturn(colorId).given(mockResources).getColor(any())
        // mock context
        val mockContext: Context = mock()
        willReturn(mockResources).given(mockContext).resources

        // WHEN
        val expectedColor = mockContext.getCompatColor(colorId)

        // THEN
        assertThat(expectedColor, `is`(2131099793))
    }
}