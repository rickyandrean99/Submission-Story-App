package com.rickyandrean.a2320j2802_submissionintermediate.ui.login

import android.util.Log
import androidx.lifecycle.*
import com.rickyandrean.a2320j2802_submissionintermediate.model.AuthenticationResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: UserPreference) : ViewModel() {
    private val _emailValid = MutableLiveData<Boolean>()
    private val _passwordValid = MutableLiveData<Boolean>()
    private val _loginStatus = MutableLiveData<Boolean>()

    val emailValid: LiveData<Boolean> = _emailValid
    val passwordValid: LiveData<Boolean> = _passwordValid
    val loginStatus: LiveData<Boolean> = _loginStatus

    init {
        _emailValid.value = false
        _passwordValid.value = false
        _loginStatus.value = false
    }

    fun updateEmailStatus(status: Boolean) {
        _emailValid.value = status
    }

    fun updatePasswordStatus(status: Boolean) {
        _passwordValid.value = status
    }

    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)

        client.enqueue(object : Callback<AuthenticationResponse> {
            override fun onResponse(
                call: Call<AuthenticationResponse>,
                response: Response<AuthenticationResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (result != null) {
                        if (!result.error) {
                            _loginStatus.value = true

                            viewModelScope.launch {
                                preference.login(
                                    UserModel(
                                        result.loginResult?.name.toString(),
                                        email,
                                        password,
                                        result.loginResult?.token.toString()
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "Authentication Failed")
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                Log.e(TAG, "Error message: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}