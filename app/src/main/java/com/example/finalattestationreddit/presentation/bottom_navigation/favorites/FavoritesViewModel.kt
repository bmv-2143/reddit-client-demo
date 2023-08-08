package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel : ViewModel() {


    private val _togglesStateFlow : MutableStateFlow<FavoritesFragmentTogglesState> =
        MutableStateFlow(FavoritesFragmentTogglesState.SubredditsAll)
    val togglesStateFlow = _togglesStateFlow.asStateFlow()

    private var subredditsToggleSelected = true
    private var postsToggleSelected = false
    private var allToggleSelected = true
    private var savedToggleSelected = false

    internal fun onSubredditsToggleSelected() {
        subredditsToggleSelected = true
        postsToggleSelected = false

        if (allToggleSelected) {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.SubredditsAll
        } else {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.SubredditsSaved
        }
    }
    internal fun onPostsToggleSelected() {
        postsToggleSelected = true
        subredditsToggleSelected = false

        if (allToggleSelected) {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.PostsAll
        } else {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.PostsSaved
        }
    }
    internal fun onAllToggleSelected() {
        allToggleSelected = true
        savedToggleSelected = false

        if (subredditsToggleSelected) {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.SubredditsAll
        } else {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.PostsAll
        }
    }

    internal fun onSavedToggleSelected() {
        savedToggleSelected = true
        allToggleSelected = false

        if (subredditsToggleSelected) {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.SubredditsSaved
        } else {
            _togglesStateFlow.value = FavoritesFragmentTogglesState.PostsSaved
        }
    }

}