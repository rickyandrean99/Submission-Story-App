package com.rickyandrean.a2320j2802_submissionintermediate.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.model.StoryResponse
import com.rickyandrean.a2320j2802_submissionintermediate.model.UserModel
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig
import com.rickyandrean.a2320j2802_submissionintermediate.storage.UserPreference
import com.rickyandrean.a2320j2802_submissionintermediate.ui.login.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preference: UserPreference) : ViewModel() {
    private val _stories = MutableLiveData<ArrayList<ListStoryItem>>()

    val stories: LiveData<ArrayList<ListStoryItem>> = _stories

    fun getStories(token: String) {
        val client = ApiConfig.getApiService().stories("Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    if (result != null) {
                        if (result.listStory != null) {
                            // Already do null checking but IDE still show red line in code below
                            // So I decide to add non-null asserted
                            _stories.value = result.listStory!!
                        }
                    }
                } else {
                    Log.e(TAG, "Error isn't successful")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "Error message: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}