package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.unsplashattestationproject.databinding.FragmentSubredditsBinding
import com.google.android.material.tabs.TabLayoutMediator

class SubredditsFragment : ViewBindingFragment<FragmentSubredditsBinding>() {

    private val subredditsViewModel: SubredditsViewModel by viewModels()

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsBinding =
        FragmentSubredditsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = binding.textHome
        subredditsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        initTabs()
    }

    private fun initTabs() {
        val viewPager = binding.fragmentSubredditsTabViewPager
        val tabLayout = binding.fragmentSubredditsTabTabLayout

        val adapter = SubredditsTabsAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "New" // TODO: hardcoded
                1 -> "Popular"
                else -> null
            }
        }
        tabLayoutMediator.attach()
    }



    override fun cleanUp() {
        tabLayoutMediator.detach()
        binding.fragmentSubredditsTabTabLayout.removeAllTabs()
        binding.fragmentSubredditsTabViewPager.adapter = null // fix memory leak
    }
}