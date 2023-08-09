package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.GetAllRecentPostsUseCase
import com.example.finalattestationreddit.domain.GetPostsUseCase
import com.example.finalattestationreddit.domain.GetUserPostsUseCase
import com.example.finalattestationreddit.log.TAG
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
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getAllRecentPostsUseCase: GetAllRecentPostsUseCase
) : ViewModel() {

    private fun getSubredditPostsFlow(subredditDisplayName: String): Flow<PagingData<Post>> {
        return getPostsUseCase(subredditDisplayName).cachedIn(viewModelScope)
    }

    private fun getUserPostsFlow(userName: String): Flow<PagingData<Post>> {
        return getUserPostsUseCase(userName).cachedIn(viewModelScope)
    }

    private var postsTarget: String = ""

    /**
     * @param target - subreddit name, any user name, logged in in this app user name
     */
    internal fun setPostsTarget(target: String) {
        postsTarget = target
    }

    fun getPostsFlow(
        postsListType: PostsListType,
    ): Flow<PagingData<Post>> {

        return when (postsListType) {

            SUBREDDIT_POSTS -> getSubredditPostsFlow(postsTarget)

            USER_POSTS -> getUserPostsFlow(postsTarget)

            SAVED_POSTS -> {
                getSubredditPostsFlow("AskReddit") // todo: this is a stub
            }

            ALL_POSTS -> getAllRecentPostsUseCase().cachedIn(viewModelScope)

        }
    }

}