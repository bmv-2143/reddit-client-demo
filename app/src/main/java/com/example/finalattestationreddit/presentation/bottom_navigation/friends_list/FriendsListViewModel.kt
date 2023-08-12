package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.domain.GetFriendsUseCase
import com.example.finalattestationreddit.domain.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val _friends = MutableSharedFlow<List<User>>()
    internal val friends = _friends.asSharedFlow()

    internal fun getFriends() {
        viewModelScope.launch() {
            val friends = getFriendsUseCase()
            val users = loadMatchingUsers(friends)
            _friends.emit(users)
        }
    }

    private suspend fun loadMatchingUsers(
        friends: List<Friend>
    ): List<User> = friends.map { friend ->
        getUserUseCase(friend.name)
    }.filterNotNull()

}