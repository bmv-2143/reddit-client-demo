package com.example.finalattestationreddit.presentation.bottom_navigation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.BuildConfig
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.DownVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.UnVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.UpVotePostOrCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostInfoViewModel @Inject constructor(
    private val upVoteUseCase: UpVotePostOrCommentUseCase,
    private val downVoteUseCase: DownVotePostOrCommentUseCase,
    private val unVoteUseCase : UnVotePostOrCommentUseCase,
    ) : ViewModel() {

    internal fun getShareLink(post : Post?) : String {
        return "${BuildConfig.REDDIT_BASE_URL}${post?.permalink}"
    }

    internal fun upVote(post : Post) {
        viewModelScope.launch() {
            upVoteUseCase(post.name)
        }
    }

    internal fun downVote(post : Post) {
        viewModelScope.launch() {
            downVoteUseCase(post.name)
        }
    }

    internal fun unVote(post : Post) {
        viewModelScope.launch() {
            unVoteUseCase(post.name)
        }
    }

}