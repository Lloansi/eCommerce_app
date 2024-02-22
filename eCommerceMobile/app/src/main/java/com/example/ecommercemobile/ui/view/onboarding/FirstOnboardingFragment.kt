package com.example.ecommercemobile.ui.view.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.FragmentFirstOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstOnboardingFragment : Fragment() {
    lateinit var binding: FragmentFirstOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstOnboardingBinding.inflate(inflater, container, false)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.nextBtn.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.btnSkip.setOnClickListener {
            onBoardingFinished()
            val toLogin = OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
            findNavController().navigate(toLogin)
        }

        return binding.root
    }
    private fun onBoardingFinished(){
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("Finished", true)
            .apply()
    }
}