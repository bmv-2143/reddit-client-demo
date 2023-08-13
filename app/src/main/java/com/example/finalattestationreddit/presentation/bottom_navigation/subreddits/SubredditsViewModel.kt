package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.SearchSubredditsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubredditsViewModel @Inject constructor(private val searchSubredditsUseCase: SearchSubredditsUseCase): ViewModel() {

    internal fun searchSubreddits(query: String): Flow<PagingData<SubredditData>> {
        return searchSubredditsUseCase(query).cachedIn(viewModelScope)
    }

}