package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ecommercemobile.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    lateinit var binding: FragmentHelpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHelpBinding.inflate(inflater, container, false)
        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }


}