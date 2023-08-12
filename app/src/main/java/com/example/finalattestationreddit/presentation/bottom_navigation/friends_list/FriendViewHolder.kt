package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.databinding.ListItemFriendBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.image_utils.ImageUtils
import com.example.finalattestationreddit.presentation.utils.formatLargeNumber


class FriendViewHolder(
    private val binding: ListItemFriendBinding,
    private val onClick: (User) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: User? = null

    init {
        setFriendListItemClickListener()
    }

    private fun setFriendListItemClickListener() {
        binding.root.setOnClickListener {
            currentItem?.let(onClick)
        }
    }

    fun bind(friend: User) {
        currentItem = friend
        setUserTexts(friend)
        friend.iconImg?.let { loadUserAvatar(it) }
    }

    private fun setUserTexts(friend: User) {
        binding.listItemFriendUserName.text = friend.name
        binding.listItemFriendKarma.text = binding.root.context.getString(
            R.string.fragment_friends_list_friend_item_karma,
            formatLargeNumber(friend.totalKarma)
        )
    }

    private fun loadUserAvatar(avatarUrl : String) {
        ImageUtils().loadCircularAvatar(binding.root.context, avatarUrl, binding.listItemFriendUserAvatar)
    }
}

