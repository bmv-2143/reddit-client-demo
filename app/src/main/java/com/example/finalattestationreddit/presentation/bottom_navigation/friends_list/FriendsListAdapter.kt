package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.databinding.ListItemFriendBinding

class FriendsListAdapter(private val onItemClick: (User) -> Unit) :
    ListAdapter<User, FriendViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ListItemFriendBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return FriendViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}