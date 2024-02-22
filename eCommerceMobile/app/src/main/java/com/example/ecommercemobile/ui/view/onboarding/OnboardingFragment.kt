package com.example.ecommercemobile.ui.view.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercemobile.databinding.FragmentOnboardingBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    lateinit var binding: FragmentOnboardingBinding
    lateinit var userViewModel: UserViewModel
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingFragment(),
            SecondOnboardingFragment(),
            ThirdOnboardingFragment()
        )

        val adapter = OnboardingAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter
        return binding.root
    }
}