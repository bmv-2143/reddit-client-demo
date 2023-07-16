package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.PlaceholderContent.PlaceholderItem
import com.example.unsplashattestationproject.databinding.ListItemSubredditBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class SubredditListItemRecyclerViewAdapter(
    private var values: List<PlaceholderItem>
) : RecyclerView.Adapter<SubredditListItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ListItemSubredditBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    fun setData(newValues: List<PlaceholderItem>) {
        values = newValues
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ListItemSubredditBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val idView: TextView = binding.listItemSubredditItemNumber
        val contentView: TextView = binding.listItermSubredditContent

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}