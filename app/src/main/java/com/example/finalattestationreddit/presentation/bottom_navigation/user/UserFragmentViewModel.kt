package com.example.finalattestationreddit.presentation.bottom_navigation.user

import androidx.lifecycle.ViewModel
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.domain.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class UserFragmentViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow = _userFlow.asStateFlow()

    suspend fun getUser(userName: String) {
        _userFlow.value = getUserUseCase(userName)
    }
}