package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.GetPostsUseCase
import com.example.finalattestationreddit.domain.GetUserPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase
) : ViewModel() {

    internal fun getPostsFlow(subredditDisplayName: String): Flow<PagingData<Post>> {
        return getPostsUseCase(subredditDisplayName).cachedIn(viewModelScope)
    }

    internal fun getUserPostsFlow(userName: String): Flow<PagingData<Post>> {
        return getUserPostsUseCase(userName).cachedIn(viewModelScope)
    }
}