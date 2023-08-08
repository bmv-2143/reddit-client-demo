package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.GetPostsUseCase
import com.example.finalattestationreddit.domain.GetUserPostsUseCase
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.ALL_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.SAVED_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.SUBREDDIT_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.USER_POSTS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase
) : ViewModel() {

    private fun getSubredditPostsFlow(subredditDisplayName: String): Flow<PagingData<Post>> {
        return getPostsUseCase(subredditDisplayName).cachedIn(viewModelScope)
    }

    private fun getUserPostsFlow(userName: String): Flow<PagingData<Post>> {
        return getUserPostsUseCase(userName).cachedIn(viewModelScope)
    }

    fun getPostsFlow(
        postsListType: PostsListType,
        subredditOrUserName: String
    ): Flow<PagingData<Post>> {
        return when (postsListType) {
            SUBREDDIT_POSTS -> getSubredditPostsFlow(subredditOrUserName)
            USER_POSTS -> getUserPostsFlow(subredditOrUserName)
            SAVED_POSTS -> {
                getSubredditPostsFlow("AskReddit") // todo: this is a stub
            }
            ALL_POSTS -> {
                getSubredditPostsFlow("Home") // todo: this is a stub
            }
        }
    }

}