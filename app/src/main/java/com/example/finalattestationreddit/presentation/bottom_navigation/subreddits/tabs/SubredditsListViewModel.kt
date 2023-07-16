package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditsListViewModel @Inject constructor(
    private val repository: RedditRepository
): ViewModel() {


    private val _subredditsFlow = MutableSharedFlow<List<SubredditData>>()
    internal val subredditsFlow = _subredditsFlow.asSharedFlow()


    internal fun getNewSubreddits() {

        viewModelScope.launch {
            val result = repository.getNewSubreddits()
            Log.e(TAG, "getNewSubreddits: $result")
            _subredditsFlow.emit(result)
        }

    }

}