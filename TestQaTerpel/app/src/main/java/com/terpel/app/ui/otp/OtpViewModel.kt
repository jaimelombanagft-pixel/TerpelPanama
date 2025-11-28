package com.terpel.app.ui.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {
    private val _remaining = MutableLiveData(60)
    val remaining: LiveData<Int> = _remaining

    private val _canResend = MutableLiveData(false)
    val canResend: LiveData<Boolean> = _canResend

    private val _verified = MutableLiveData(false)
    val verified: LiveData<Boolean> = _verified

    private var timerJob: Job? = null

    fun startTimer() {
        timerJob?.cancel()
        _remaining.value = 60
        _canResend.value = false
        timerJob = viewModelScope.launch {
            while (_remaining.value!! > 0) {
                delay(1000)
                _remaining.value = _remaining.value!! - 1
            }
            _canResend.value = true
        }
    }

    fun verify(code: String) {
        _verified.value = code.length == 6
    }
}
