package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.databinding.ListItemFriendBinding

class FriendsListAdapter(private val onFriendClick: (Friend) -> Unit) : ListAdapter<Friend, FriendViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {

        val binding = ListItemFriendBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return FriendViewHolder(binding, onFriendClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Friend>() {
            override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
                return oldItem == newItem
            }
        }
    }
}