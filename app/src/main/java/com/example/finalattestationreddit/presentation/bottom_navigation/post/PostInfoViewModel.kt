package com.example.finalattestationreddit.presentation.bottom_navigation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.BuildConfig
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.DownVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.GetPostUseCase
import com.example.finalattestationreddit.domain.SaveUnsavePostUseCase
import com.example.finalattestationreddit.domain.UnVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.UpVotePostOrCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostInfoViewModel @Inject constructor(
    private val upVoteUseCase: UpVotePostOrCommentUseCase,
    private val downVoteUseCase: DownVotePostOrCommentUseCase,
    private val unVoteUseCase: UnVotePostOrCommentUseCase,
    private val getPostUseCase: GetPostUseCase,
    private val saveUnsavePostUseCase: SaveUnsavePostUseCase
) : ViewModel() {

    internal fun getShareLink(post: Post?): String {
        return "${BuildConfig.REDDIT_BASE_URL}${post?.permalink}"
    }

    private val _updatedPostFlow = MutableStateFlow<Post?>(null)
    val updatedPostFlow = _updatedPostFlow.asStateFlow()

    internal fun upVote(post: Post) {
        viewModelScope.launch() {
            if (post.likedByUser == true) {
                unVoteUseCase(post.name)
            } else {
                upVoteUseCase(post.name)
            }
            fetchUpdatedPost(post)
        }
    }

    private suspend fun fetchUpdatedPost(post: Post) {
        _updatedPostFlow.value = getPostUseCase(post.name)
    }

    internal fun downVote(post: Post) {
        viewModelScope.launch() {
            if (post.likedByUser == false) {
                unVoteUseCase(post.name)
            } else {
                downVoteUseCase(post.name)
            }
            fetchUpdatedPost(post)
        }
    }

    internal fun shouldDisplayShowAllCommentsButton(post : Post) : Boolean =
        post.numComments > BuildConfig.POST_COMMENTS_PAGE_SIZE_MIN

    internal fun switchPostSavedState(post : Post) {
        viewModelScope.launch {
            val success = saveUnsavePostUseCase(post.name, !post.saved)
            if (success) {
                _updatedPostFlow.value = post.copy(saved = !post.saved)
                fetchUpdatedPost(post)
            }
        }
    }

}