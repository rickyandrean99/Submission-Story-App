package com.rickyandrean.a2320j2802_submissionintermediate.di

import com.rickyandrean.a2320j2802_submissionintermediate.data.StoryRepository
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}