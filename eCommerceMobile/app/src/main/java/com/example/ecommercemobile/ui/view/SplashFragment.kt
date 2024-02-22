package com.example.ecommercemobile.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.databinding.FragmentSplashBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("SPLASH ON CREATE VIEW")
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        setTheme()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            println("SPLASH-User: $user")
            if(user!=null) {
                userViewModel.getUserPicture(user.userID)
                val toHome = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(toHome)
            }
        }

        userViewModel.authResult.observe(viewLifecycleOwner) { authResult ->
            println("SPLASH-AutResult: $authResult")
            if(authResult is AuthResult.Authorized) {
                userViewModel.getUser()

            } else {
                val toLogin = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(toLogin)
            }
        }

        val onBoardingFinished = onBoardingFinished()
        println("SPLASH-OnboardingFinished: $onBoardingFinished")
        if (!onBoardingFinished) {
            val toOnboarding = SplashFragmentDirections.actionSplashFragmentToOnboardingFragment()
            findNavController().navigate(toOnboarding)
        }
    }

    private fun onBoardingFinished(): Boolean{
        return prefs.getBoolean("Finished", false)
    }

    private fun setTheme() {
        prefs.getBoolean("darkmode", false).let { darkmode ->
            if (darkmode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}