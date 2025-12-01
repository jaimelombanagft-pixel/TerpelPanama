package com.terpel.app.ui.register

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terpel.app.data.model.RegisterResponse
import com.terpel.app.data.repository.RegisterRepository
import kotlinx.coroutines.launch

data class RegisterFormState(
    val fullName: String = "",
    val lastName: String = "",
    val email: String = "",
    val document: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val vehicleType: String = "",
    val documentType: String = "CC",
    val nameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val documentError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val registrationResponse: RegisterResponse? = null
)

class RegisterViewModel : ViewModel() {
    private val _state = MutableLiveData(RegisterFormState())
    val state: LiveData<RegisterFormState> = _state

    private val repository = RegisterRepository()

    fun onNameChanged(value: String) {
        updateAndValidate(_state.value!!.copy(fullName = value))
    }

    fun onLastNameChanged(value: String) {
        _state.value = _state.value!!.copy(lastName = value)
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

    fun onDocumentTypeChanged(value: String) {
        _state.value = _state.value!!.copy(documentType = value)
    }

    fun onVehicleTypeChanged(value: String) {
        _state.value = _state.value!!.copy(vehicleType = value)
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

    fun registerUser() {
        val s = _state.value ?: return
        _state.value = s.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            android.util.Log.d("RegisterViewModel", "Iniciando registro con datos: fullName=${s.fullName}, email=${s.email}, vehicleType=${s.vehicleType}")
            
            val result = repository.registerUser(
                firstName = s.fullName,
                lastName = s.lastName,
                documentType = s.documentType,
                document = s.document,
                cellPhone = s.phone,
                email = s.email,
                vehicleType = s.vehicleType
            )
            
            result.onSuccess { response ->
                android.util.Log.d("RegisterViewModel", "Registro exitoso: ${response.message}")
                _state.value = _state.value!!.copy(
                    isLoading = false,
                    isSuccess = true,
                    registrationResponse = response,
                    errorMessage = null
                )
            }
            
            result.onFailure { error ->
                val errorMsg = error.message ?: "Error desconocido durante el registro"
                android.util.Log.e("RegisterViewModel", "Error en registro: $errorMsg", error)
                _state.value = _state.value!!.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = errorMsg
                )
            }
        }
    }
}

