package com.example.finalattestationreddit.presentation.bottom_navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.dto.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BottomNavigationActivityViewModel @Inject constructor(
    application: Application,
    redditRepository: RedditRepository
) : AndroidViewModel(application) {

    val networkErrorsFlow = redditRepository.networkErrorsFlow

    private val _selectedPostFlow = MutableStateFlow<Post?>(null)
    val selectedPostFlow = _selectedPostFlow.asStateFlow()

    internal fun setSelectedPost(post : Post) {
        _selectedPostFlow.value = post
    }

}