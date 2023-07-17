package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.unsplashattestationproject.databinding.ListItemSubredditBinding

class SubredditsAdapterViewHolder(
    private val binding: ListItemSubredditBinding,
    val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: SubredditData? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it.displayName)
            }
        }
    }

    fun bind(subredditItem: SubredditData) {
        currentItem = subredditItem
        loadTexts(subredditItem)
//        loadAvatar(subredditItem)
//        loadPhoto(subredditItem.coverPhoto?.urls?.regular ?: "")
    }

    private fun loadTexts(subredditItem: SubredditData) {

        binding.listItemSubredditItemNumber.text = subredditItem.id
        binding.listItermSubredditTitle.text = subredditItem.displayName // TODO: fix me
        binding.listItermSubredditDescription.text = subredditItem.publicDescription

//        binding.collectionListItemNumberOfPhotos.text =
//            binding.root.context.getString(
//                R.string.collection_item_number_of_photos_template,
//                collectionItem.totalPhotos.toString()
//            )
//
//        binding.collectionListItemTitle.text = collectionItem.title
//        binding.collectionAuthorName.text = collectionItem.user.name
//
//        binding.collectionAuthorNickname.text =
//            binding.root.context.getString(
//                R.string.photo_item_author_nickname_template,
//                collectionItem.user.username
//            )
    }

//    private fun loadAvatar(collectionItem: UnsplashCollection) {
//        Glide.with(binding.root)
//            .load(collectionItem.user.profileImageUrls.small)
//            .circleCrop()
//            .placeholder(R.drawable.photo_list_item_avatar_placeholder)
//            .into(binding.collectionAuthorAvatar)
//    }
//
//    private fun loadPhoto(imageUrl: String) {
//        progressBarSetVisible(binding, true)
//
//        Glide.with(binding.root.context)
//            .load(imageUrl)
//            .placeholder(R.drawable.photo_list_item_image_placeholder)
//            .listener(object : RequestListener<Drawable> {
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    progressBarSetVisible(binding, false)
//                    return false
//                }
//
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    Log.e(TAG, "Glide onLoadFailed: $imageUrl")
//                    return false
//                }
//            })
//            .into(binding.collectionItemImage)
//    }
//
//    private fun progressBarSetVisible(binding: CollectionListItemBinding, isActive: Boolean) =
//        if (isActive) {
//            binding.collectionItemProgressBar.visibility = android.view.View.VISIBLE
//        } else {
//            binding.collectionItemProgressBar.visibility = android.view.View.GONE
//        }
}