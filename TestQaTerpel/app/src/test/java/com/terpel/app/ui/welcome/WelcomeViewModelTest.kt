package com.terpel.app.ui.welcome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WelcomeViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun pages_are_available() {
        val vm = WelcomeViewModel()
        val value = vm.pages.value
        assertTrue(value != null && value.isNotEmpty())
    }
}

