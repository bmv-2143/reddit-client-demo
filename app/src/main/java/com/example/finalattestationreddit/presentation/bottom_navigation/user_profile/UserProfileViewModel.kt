package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is User Profile Fragment"
    }
    val text: LiveData<String> = _text
}