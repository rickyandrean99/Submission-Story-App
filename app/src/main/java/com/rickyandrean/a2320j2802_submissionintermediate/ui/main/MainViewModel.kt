package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val preference: UserPreference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }

//    fun getUser(): LiveData<UserModel> {
//        return preference.getUser().asLiveData()
//    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}