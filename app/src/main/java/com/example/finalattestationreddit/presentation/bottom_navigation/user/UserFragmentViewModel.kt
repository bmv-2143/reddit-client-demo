package com.example.finalattestationreddit.presentation.bottom_navigation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.domain.AddUserUseCase
import com.example.finalattestationreddit.domain.CheckIfIsAFriendUseCase
import com.example.finalattestationreddit.domain.GetUserPostsCountUseCase
import com.example.finalattestationreddit.domain.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserFragmentViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserPostsCountUseCase: GetUserPostsCountUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val isFriendUseCase: CheckIfIsAFriendUseCase
) : ViewModel() {

    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow = _userFlow.asStateFlow()

    internal fun getUser(userName: String) {
        viewModelScope.launch {
            _userFlow.value = getUserUseCase(userName)
        }
    }

    private val _userPostsCountFlow = MutableStateFlow(0)
    val userPostsCountFlow = _userPostsCountFlow.asStateFlow()

    internal fun loadUserPostsCount(userName: String) {
        viewModelScope.launch {
            _userPostsCountFlow.value = getUserPostsCountUseCase(userName)
        }
    }

    internal fun addFriend() {
        viewModelScope.launch {
            userFlow.value?.name?.let {
                _isFriendFlow.value = addUserUseCase(it)
            }
        }
    }

    private val _isFriendFlow = MutableStateFlow(false)
    val isFriendFlow = _isFriendFlow.asStateFlow()

    internal fun checkIfIsAFriend(userName: String) {
        viewModelScope.launch {
            _isFriendFlow.value = isFriendUseCase(userName)
        }
    }

}