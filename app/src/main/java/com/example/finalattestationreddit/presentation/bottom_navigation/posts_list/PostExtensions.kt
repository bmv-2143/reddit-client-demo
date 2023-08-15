package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import com.example.finalattestationreddit.data.model.dto.post.Post


fun Post.getFirstUrlOrNull(): String? = preview?.images?.firstOrNull()?.source?.url