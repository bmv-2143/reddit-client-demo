package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.unsplashattestationproject.databinding.FragmentSubredditsBinding
import com.google.android.material.tabs.TabLayoutMediator

class SubredditsFragment : Fragment() {

    private var _binding: FragmentSubredditsBinding? = null
    private val binding get() = _binding!!

    private val subredditsViewModel: SubredditsViewModel by viewModels()

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubredditsBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textHome
        subredditsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabs()
    }

    private fun initTabs() {
        val viewPager = binding.fragmentSubredditsTabViewPager
        val tabLayout = binding.fragmentSubredditsTabTabLayout

        val adapter = SubredditsTabsAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "New"
                1 -> "Popular"
                else -> null
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onResume() {
        super.onResume()
        hideActionbar()
    }

    private fun hideActionbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        showActionBar()
    }

    private fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanUp()
        _binding = null
    }

    private fun cleanUp() {
        tabLayoutMediator.detach()
        _binding?.fragmentSubredditsTabTabLayout?.removeAllTabs()
        _binding?.fragmentSubredditsTabViewPager?.adapter = null // fix memory leak
    }
}