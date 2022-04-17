package com.rickyandrean.a2320j2802_submissionintermediate.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.a2320j2802_submissionintermediate.model.AuthenticationResponse
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val preference: UserPreference) : ViewModel() {
    private val _emailValid = MutableLiveData<Boolean>()
    private val _passwordValid = MutableLiveData<Boolean>()

    val emailValid: LiveData<Boolean> = _emailValid
    val passwordValid: LiveData<Boolean> = _passwordValid

    init {
        _emailValid.value = false
        _passwordValid.value = false
    }

    fun updateEmailStatus(status: Boolean) {
        _emailValid.value = status
    }

    fun updatePasswordStatus(status: Boolean) {
        _passwordValid.value = status
    }

    fun register(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<AuthenticationResponse> {
            override fun onResponse(
                call: Call<AuthenticationResponse>,
                response: Response<AuthenticationResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (result != null) {
                        if (!result.error) {
                            Log.d(TAG, "Account Successfully Created")
                        }
                    }
                } else {
                    // Program will enter this block of code if the email isn't valid by Dicoding backend system.
                    // For example 'ricky@gmail.c' is true when using kotlin email checking but false by backend
                    Log.e(TAG, "Please input valid email address")
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                Log.e(TAG, "Error message: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}