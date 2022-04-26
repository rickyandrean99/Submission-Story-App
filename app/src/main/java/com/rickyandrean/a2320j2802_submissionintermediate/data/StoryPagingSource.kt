package com.rickyandrean.a2320j2802_submissionintermediate.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rickyandrean.a2320j2802_submissionintermediate.model.ListStoryItem
import com.rickyandrean.a2320j2802_submissionintermediate.network.ApiService

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXR5UDc2M2xhUmZFRHdiZFYiLCJpYXQiOjE2NTA5MzMzNzJ9.v78NKqhAkojTgCc5pBw4wZLpp6ucc0eQHbEsoa5dQuw"

        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.storiesPaging(token, position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory!!,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}