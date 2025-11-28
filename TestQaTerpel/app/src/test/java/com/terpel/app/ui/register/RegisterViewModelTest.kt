package com.terpel.app.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun invalid_email_sets_error() {
        val vm = RegisterViewModel()
        vm.onEmailChanged("bad")
        val state = vm.state.value!!
        assertEquals("Email inválido", state.emailError)
        assertFalse(state.isValid)
    }

    @Test
    fun confirm_mismatch_sets_error() {
        val vm = RegisterViewModel()
        vm.onPasswordChanged("12345678")
        vm.onConfirmChanged("12345679")
        val state = vm.state.value!!
        assertEquals("No coincide", state.confirmError)
        assertFalse(state.isValid)
    }

    @Test
    fun valid_form_is_valid() {
        val vm = RegisterViewModel()
        vm.onNameChanged("Usuario Test")
        vm.onEmailChanged("user@test.com")
        vm.onPasswordChanged("12345678")
        vm.onConfirmChanged("12345678")
        val state = vm.state.value!!
        assertTrue(state.isValid)
    }
}

