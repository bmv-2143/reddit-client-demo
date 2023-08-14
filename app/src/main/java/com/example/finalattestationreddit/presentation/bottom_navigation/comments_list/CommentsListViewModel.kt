package com.example.finalattestationreddit.presentation.bottom_navigation.comments_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.domain.DownVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.GetCommentUseCase
import com.example.finalattestationreddit.domain.GetPostCommentsUseCase
import com.example.finalattestationreddit.domain.UnVotePostOrCommentUseCase
import com.example.finalattestationreddit.domain.UpVotePostOrCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsListViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val upVoteUseCase: UpVotePostOrCommentUseCase,
    private val downVoteUseCase: DownVotePostOrCommentUseCase,
    private val unVoteUseCase: UnVotePostOrCommentUseCase,
    private val getCommentUseCase: GetCommentUseCase
) : ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun startLoadingPostComments(
        subredditDisplayName: String,
        postName: String,
        commentsCountLimit: Int
    ) {
        viewModelScope.launch {
            val filteredComments = getPostCommentsUseCase(
                subredditDisplayName,
                postName,
                commentsCountLimit
            ).filter { comment -> comment.body != null }
            _comments.value = filteredComments
        }
    }


    private val _updatedCommentsFlow = MutableStateFlow<Comment?>(null)
    val updatedCommentsFlow = _updatedCommentsFlow.asStateFlow()

    internal fun upVote(comment: Comment) {
        viewModelScope.launch() {
            if (comment.likedByUser == true) {
                unVoteUseCase(comment.name)
            } else {
                upVoteUseCase(comment.name)
            }
            fetchUpdatedComment(comment)
        }
    }

    private suspend fun fetchUpdatedComment(comment: Comment) {
        _updatedCommentsFlow.value = getCommentUseCase(comment.name)
    }

    internal fun downVote(comment: Comment) {
        viewModelScope.launch() {
            if (comment.likedByUser == false) {
                unVoteUseCase(comment.name)
            } else {
                downVoteUseCase(comment.name)
            }
            fetchUpdatedComment(comment)
        }
    }

}