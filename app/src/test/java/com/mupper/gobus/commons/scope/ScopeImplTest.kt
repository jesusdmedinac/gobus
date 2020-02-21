package com.mupper.gobus.commons.scope

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class ScopeImplTest {
    private lateinit var expectedDispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        expectedDispatcher = Dispatchers.Default
    }

    @Test
    fun `ScopeImpl should set uiDispatcher as given dispatcher`() {
        // GIVEN default coroutine dispatcher

        // WHEN
        val scopeImpl = Scope.Impl(expectedDispatcher)

        // THEN
        assertThat(scopeImpl.uiDispatcher, `is`(expectedDispatcher))
    }

    @Test
    fun `coroutineContext should return an instance of given SupervisorJob`() {
        // GIVEN
        val scopeImpl = Scope.Impl(expectedDispatcher)

        // WHEN
        scopeImpl.initScope()

        // THEN
        assertThat(scopeImpl.coroutineContext.toString(), containsString("SupervisorJobImpl"))
    }

    @Test
    fun `coroutineContext should return an instance of given dispatcher`() {
        // GIVEN
        val scopeImpl = Scope.Impl(expectedDispatcher)

        // WHEN
        scopeImpl.initScope()

        // THEN
        assertThat(scopeImpl.coroutineContext.toString(), containsString("DefaultDispatcher"))
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `coroutineContext should throw UninitializedPropertyAccessException if job has not been initialized`() {
        // GIVEN
        val scopeImpl = Scope.Impl(Dispatchers.Unconfined)

        // WHEN
        scopeImpl.coroutineContext

        // THEN throw exception
    }

    @Test
    fun `initScope should create a job as SupervisorJob`() {
        // GIVEN
        val scopeImpl = Scope.Impl(Dispatchers.Unconfined)

        // WHEN
        scopeImpl.initScope()

        // THEN
        assertThat(scopeImpl.job.toString(), containsString("SupervisorJobImpl"))
    }

    @Test
    fun `initScope should not cancel the ScopeImpl job`() {
        // GIVEN
        val scopeImpl = Scope.Impl(Dispatchers.Unconfined)

        // WHEN
        scopeImpl.initScope()

        // THEN
        assertThat(scopeImpl.job.isCancelled, `is`(false))
    }

    @Test
    fun `destroyScope should cancel the ScopeImpl job`() {
        // GIVEN
        val scopeImpl = Scope.Impl(Dispatchers.Unconfined)
        scopeImpl.initScope()

        // WHEN
        scopeImpl.destroyScope()

        // THEN
        assertThat(scopeImpl.job.isCancelled, `is`(true))
    }
}