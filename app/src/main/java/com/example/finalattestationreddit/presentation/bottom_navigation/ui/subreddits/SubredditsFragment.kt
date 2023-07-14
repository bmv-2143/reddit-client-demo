package com.example.finalattestationreddit.presentation.bottom_navigation.ui.subreddits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.unsplashattestationproject.databinding.FragmentSubredditsBinding

class SubredditsFragment : Fragment() {

    private var _binding: FragmentSubredditsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val subredditsViewModel =
            ViewModelProvider(this)[SubredditsViewModel::class.java]

        _binding = FragmentSubredditsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        subredditsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}