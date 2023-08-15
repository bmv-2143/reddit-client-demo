package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.subreddits.SearchSubredditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SubredditsSearchViewModel @Inject constructor(
    private val searchSubredditsUseCase: SearchSubredditsUseCase
) : ViewModel() {

    internal fun searchSubreddits(query: String): Flow<PagingData<SubredditData>> {
        return searchSubredditsUseCase(query).cachedIn(viewModelScope)
    }

}