package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rickyandrean.a2320j2802_submissionintermediate.data.StoryRepository
import com.rickyandrean.a2320j2802_submissionintermediate.di.Injection
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.model.StoryResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preference: UserPreference, private val storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
}

class ViewModelMainFactory(private val pref: UserPreference, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(pref, Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}