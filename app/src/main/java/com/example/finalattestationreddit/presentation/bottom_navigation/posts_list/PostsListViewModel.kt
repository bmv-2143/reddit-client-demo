package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.domain.GetAllRecentPostsUseCase
import com.example.finalattestationreddit.domain.GetMySavedPostsUseCase
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
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getAllRecentPostsUseCase: GetAllRecentPostsUseCase,
    private val getMySavedPostsUseCase: GetMySavedPostsUseCase
) : ViewModel() {


    private var postsTarget: String = ""

    /**
     * @param target - subreddit name, any user name, logged in in this app user name
     */
    internal fun setPostsTarget(target: String) {
        postsTarget = target
    }

    fun getPostsFlow(postsListType: PostsListType) : Flow<PagingData<Post>> {
        val flow = when (postsListType) {

            SUBREDDIT_POSTS -> getPostsUseCase(postsTarget)

            USER_POSTS -> getUserPostsUseCase(postsTarget)

            SAVED_POSTS -> getMySavedPostsUseCase()

            ALL_POSTS -> getAllRecentPostsUseCase()

        }
        return flow.cachedIn(viewModelScope)
    }

}