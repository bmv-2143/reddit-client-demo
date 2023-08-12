package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.databinding.ListItemFriendBinding
import com.example.finalattestationreddit.log.TAG


class FriendViewHolder(
    private val binding: ListItemFriendBinding,
    private val onClick: (Friend) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: Friend? = null

    init {
        setFriendListItemClickListener()
    }

    private fun setFriendListItemClickListener() {
        binding.root.setOnClickListener {
            currentItem?.let(onClick)
        }
    }

    fun bind(friend: Friend) {
        currentItem = friend
        binding.listItemFriendUserName.text = friend.name

        Log.e(TAG, "bind: " + friend.name)

//        Glide.with(binding.listItemFriendUserAvatar)
//            .load(friend.avatarUrl)
//            .placeholder(R.drawable.user_placeholder_person_24)
//            .into(binding.listItemFriendUserAvatar)
    }
}
    // Use your favorite image loading library to load the avatar image from the URL
        // For example, with Glide:
        // Glide.with(avatarImageView).load(friend.avatarUrl).into(avatarImageView)
