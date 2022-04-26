package com.rickyandrean.a2320j2802_submissionintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.rickyandrean.a2320j2802_submissionintermediate.database.StoryDatabase
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            //remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
                //storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}