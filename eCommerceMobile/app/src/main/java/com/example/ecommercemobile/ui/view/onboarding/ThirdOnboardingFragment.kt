package com.example.ecommercemobile.ui.view.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.databinding.FragmentThirdOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdOnboardingFragment : Fragment() {
    lateinit var binding: FragmentThirdOnboardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdOnboardingBinding.inflate(inflater, container, false)

        binding.nextBtn.setOnClickListener {
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