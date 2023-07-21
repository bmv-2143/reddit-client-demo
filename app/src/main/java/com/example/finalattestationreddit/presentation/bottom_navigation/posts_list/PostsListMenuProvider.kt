package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.example.finalattestationreddit.R

class PostsListMenuProvider(
    private val onNavigateUpClick: () -> Unit,
    private val onSubredditInfoMenuClick: () -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_posts_lists_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            android.R.id.home -> {
                onNavigateUpClick()
                true
            }

            R.id.action_fragment_menu_subreddit_info -> {
                onSubredditInfoMenuClick()
                true
            }

            else -> false
        }
    }
}