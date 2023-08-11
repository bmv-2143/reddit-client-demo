package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.finalattestationreddit.databinding.FragmentFriendsListBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment


class FriendsListFragment : ViewBindingFragment<FragmentFriendsListBinding>() {


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendsListBinding =
        FragmentFriendsListBinding.inflate(inflater, container, false)


}