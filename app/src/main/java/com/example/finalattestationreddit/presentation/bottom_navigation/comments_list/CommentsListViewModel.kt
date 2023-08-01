package com.example.finalattestationreddit.presentation.bottom_navigation.comments_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.domain.GetPostCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsListViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
): ViewModel() {

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    fun startLoadingPostComments(subredditDisplayName: String, postName: String) {
        viewModelScope.launch {
            _comments.value = getPostCommentsUseCase(subredditDisplayName, postName)
        }
    }

}