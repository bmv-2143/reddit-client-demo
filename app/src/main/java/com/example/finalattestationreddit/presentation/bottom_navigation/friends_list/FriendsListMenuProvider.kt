package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import com.example.finalattestationreddit.R

class FriendsListMenuProvider(
    private val onMenuItemClick: () -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_friends_lists_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {

            R.id.action_fragment_menu_dummy_info -> {
                onMenuItemClick()
                true
            }

            else -> false
        }
    }
}