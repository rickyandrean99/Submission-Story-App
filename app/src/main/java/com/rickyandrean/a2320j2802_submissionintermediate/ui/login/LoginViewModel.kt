package com.rickyandrean.a2320j2802_submissionintermediate.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference

class LoginViewModel(private val preference: UserPreference): ViewModel() {
    private val _emailValid = MutableLiveData<Boolean>()
    private val _passwordValid = MutableLiveData<Boolean>()

    val emailValid: LiveData<Boolean> = _emailValid
    val passwordValid: LiveData<Boolean> = _passwordValid

    init {
        _emailValid.value = false
        _passwordValid.value = false
    }

    fun updateEmailStatus(status: Boolean){
        _emailValid.value = status
    }

    fun updatePasswordStatus(status: Boolean){
        _passwordValid.value = status
    }
}