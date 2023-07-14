package com.example.finalattestationreddit.presentation.bottom_navigation.ui.subreddits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubredditsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Subreddits Fragment"
    }
    val text: LiveData<String> = _text
}