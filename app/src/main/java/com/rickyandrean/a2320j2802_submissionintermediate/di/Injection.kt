package com.rickyandrean.a2320j2802_submissionintermediate.di

import android.content.Context
import com.rickyandrean.a2320j2802_submissionintermediate.data.StoryRepository
import com.rickyandrean.a2320j2802_submissionintermediate.database.StoryDatabase
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()

        return StoryRepository(database, apiService)
    }
}