package com.rickyandrean.a2320j2802_submissionintermediate.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference

class AddViewModel(private val preference: UserPreference) : ViewModel() {
    var token = ""

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }
}