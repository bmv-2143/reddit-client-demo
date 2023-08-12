package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.domain.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(private val getFriendsUseCase : GetFriendsUseCase)
    : ViewModel() {

    private val _friends = MutableSharedFlow<List<Friend>>()
    internal val friends = _friends.asSharedFlow()

    internal fun getFriends() {
        viewModelScope.launch() {
            _friends.emit(getFriendsUseCase())
        }
    }

}