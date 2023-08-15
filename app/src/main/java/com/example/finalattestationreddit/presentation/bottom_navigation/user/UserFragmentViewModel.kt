package com.example.finalattestationreddit.presentation.bottom_navigation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.domain.friends.AddFriendUseCase
import com.example.finalattestationreddit.domain.friends.CheckIfIsAFriendUseCase
import com.example.finalattestationreddit.domain.posts.GetUserPostsCountUseCase
import com.example.finalattestationreddit.domain.users.GetUserUseCase
import com.example.finalattestationreddit.domain.friends.RemoveFriendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFragmentViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserPostsCountUseCase: GetUserPostsCountUseCase,
    private val addFriendUseCase: AddFriendUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    private val isFriendUseCase: CheckIfIsAFriendUseCase
) : ViewModel() {

    private val _userFlow = MutableStateFlow<User?>(null)
    internal val userFlow = _userFlow.asStateFlow()

    internal fun getUser(userName: String) {
        viewModelScope.launch {
            _userFlow.value = getUserUseCase(userName)
        }
    }

    private val _userPostsCountFlow = MutableStateFlow(0)
    internal val userPostsCountFlow = _userPostsCountFlow.asStateFlow()

    internal fun loadUserPostsCount(userName: String) {
        viewModelScope.launch {
            _userPostsCountFlow.value = getUserPostsCountUseCase(userName)
        }
    }

    internal fun addFriend(userName: String) {
        viewModelScope.launch {
            _isFriendFlow.value = addFriendUseCase(userName)
        }
    }

    internal fun removeFriend(userName: String) {
        viewModelScope.launch {
            _isFriendFlow.value = removeFriendUseCase(userName)
        }
    }

    private val _isFriendFlow = MutableStateFlow(false)
    internal val isFriendFlow = _isFriendFlow.asStateFlow()

    internal fun checkIfIsAFriend(userName: String) {
        viewModelScope.launch {
            _isFriendFlow.value = isFriendUseCase(userName)
        }
    }

}