package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.finalattestationreddit.domain.GetNewSubredditsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SubredditsListViewModel @Inject constructor(
    getNewSubredditsUseCase: GetNewSubredditsUseCase
) : ViewModel() {

    val subredditsPagedFlow: Flow<PagingData<SubredditData>> =
        getNewSubredditsUseCase().cachedIn(viewModelScope)

}