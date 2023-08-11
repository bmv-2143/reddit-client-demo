package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.domain.ClearSavedPostUseCase
import com.example.finalattestationreddit.domain.GetMyUserCase
import com.example.finalattestationreddit.domain.GetUserPostsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getMyUserUseCase: GetMyUserCase,
    private val getUserPostsCountUseCase: GetUserPostsCountUseCase,
    private val clearSavedPostUseCase: ClearSavedPostUseCase,
) : ViewModel() {

    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow = _userFlow.asStateFlow()

    internal fun getUser() {
        viewModelScope.launch {
            _userFlow.value = getMyUserUseCase()
        }
    }

    private val _userPostsCountFlow = MutableStateFlow(0)
    val userPostsCountFlow = _userPostsCountFlow.asStateFlow()

    internal fun loadUserPostsCount(userName: String) {
        viewModelScope.launch {
            _userPostsCountFlow.value = getUserPostsCountUseCase(userName)
        }
    }

    private val _clearSavedPostsFlow = MutableSharedFlow<Boolean>()
    val clearSavedPostsFlow = _clearSavedPostsFlow.asSharedFlow()

    internal fun clearSavedPosts() {
        viewModelScope.launch {
            _clearSavedPostsFlow.emit(clearSavedPostUseCase())
        }
    }
}