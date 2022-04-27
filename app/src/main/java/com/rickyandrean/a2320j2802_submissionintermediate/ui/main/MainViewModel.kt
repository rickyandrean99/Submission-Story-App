package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rickyandrean.a2320j2802_submissionintermediate.data.StoryRepository
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(
    private val preference: UserPreference,
    storyRepository: StoryRepository
) : ViewModel() {
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