package com.example.finalattestationreddit.presentation.bottom_navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finalattestationreddit.data.RedditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavigationActivityViewModel @Inject constructor(
    application: Application,
    redditRepository: RedditRepository
) : AndroidViewModel(application) {

    val networkErrorsFlow = redditRepository.networkErrorsFlow

}