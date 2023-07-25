package com.example.finalattestationreddit.presentation.bottom_navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.dto.post.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class BottomNavigationActivityViewModel @Inject constructor(
    application: Application,
    redditRepository: RedditRepository
) : AndroidViewModel(application) {

    val networkErrorsFlow = redditRepository.networkErrorsFlow

    // todo: this is the overkill to use SharedFlow for this purpose, can use a variable ???
    private val _selectedPostFlow = MutableSharedFlow<Post>(replay = 1)
    val selectedPostFlow = _selectedPostFlow.asSharedFlow()

    internal fun setSelectedPost(post : Post) {
        _selectedPostFlow.tryEmit(post)
    }

}