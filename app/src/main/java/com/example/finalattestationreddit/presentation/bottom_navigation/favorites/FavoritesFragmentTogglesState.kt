package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

sealed class FavoritesFragmentTogglesState {
    object SubredditsAll : FavoritesFragmentTogglesState()
    object PostsAll : FavoritesFragmentTogglesState()
    object SubredditsSaved : FavoritesFragmentTogglesState()
    object PostsSaved : FavoritesFragmentTogglesState()
}