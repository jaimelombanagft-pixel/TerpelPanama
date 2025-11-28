package com.terpel.app.ui.register

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RegisterFormState(
    val fullName: String = "",
    val email: String = "",
    val document: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val documentError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

class RegisterViewModel : ViewModel() {
    private val _state = MutableLiveData(RegisterFormState())
    val state: LiveData<RegisterFormState> = _state

    fun onNameChanged(value: String) {
        updateAndValidate(_state.value!!.copy(fullName = value))
    }

    fun onEmailChanged(value: String) {
        updateAndValidate(_state.value!!.copy(email = value))
    }

    fun onDocumentChanged(value: String) {
        _state.value = _state.value!!.copy(document = value)
    }

    fun onPhoneChanged(value: String) {
        _state.value = _state.value!!.copy(phone = value)
    }

    fun onPasswordChanged(value: String) {
        val s = _state.value!!
        updateAndValidate(s.copy(password = value))
    }

    fun onConfirmChanged(value: String) {
        val s = _state.value!!
        updateAndValidate(s.copy(confirmPassword = value))
    }

    private fun updateAndValidate(next: RegisterFormState) {
        val nameErr = if (next.fullName.trim().length < 3) "Nombre inválido" else null
        val emailErr = if (!PatternsCompat.EMAIL_ADDRESS.matcher(next.email.trim()).matches()) "Email inválido" else null
        val pwdErr = if (next.password.length < 8) "Mínimo 8 caracteres" else null
        val confirmErr = if (next.confirmPassword != next.password) "No coincide" else null
        val valid = nameErr == null && emailErr == null && pwdErr == null && confirmErr == null
        _state.value = next.copy(
            nameError = nameErr,
            emailError = emailErr,
            documentError = null,
            phoneError = null,
            passwordError = pwdErr,
            confirmError = confirmErr,
            isValid = valid,
            isSuccess = false
        )
    }

    fun submit() {
        val s = _state.value ?: return
        if (!s.isValid || s.isLoading) return
        _state.value = s.copy(isLoading = true, isSuccess = false)
        viewModelScope.launch {
            delay(1500)
            _state.value = _state.value!!.copy(isLoading = false, isSuccess = true)
        }
    }
}
