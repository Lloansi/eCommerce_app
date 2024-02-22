package com.example.ecommercemobile.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.databinding.FragmentLoginBinding
import com.example.ecommercemobile.ui.view.dialogs.ForgetPassDialog
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("LOGIN ON CREATE VIEW")
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if(user!=null) {
                userViewModel.getUserPicture(user.userID)
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
            binding.loginBT.revertAnimation()

        }

        userViewModel.authResult.observe(viewLifecycleOwner) {
            when (it){
                is AuthResult.Authorized -> {
                    userViewModel.getUser()
                }
                is AuthResult.Unauthorized -> {
                    println("Unauthorized")
                    binding.loginBT.revertAnimation()
                }
                is AuthResult.ServerUnavailable -> {
                    println("ServerUnavailable")
                    Toast.makeText(requireContext(), "Server Unavailable", Toast.LENGTH_SHORT).show()
                    binding.loginBT.revertAnimation()
                }
                is AuthResult.NoConnection -> {
                    println("NoConnection")
                    Toast.makeText(requireContext(), "No connection", Toast.LENGTH_SHORT).show()
                    binding.loginBT.revertAnimation()
                }
                else -> {
                    println("UnknownError")
                    binding.loginBT.revertAnimation()
                }
            }
        }

        binding.forgotPassTV.setOnClickListener {
            val forgetPassPopup = ForgetPassDialog()
            forgetPassPopup.show(requireParentFragment().parentFragmentManager, ForgetPassDialog.TAG)
        }

        binding.loginBT.setOnClickListener {
            val email = binding.emailET.editText?.text.toString()
            val pswrd =  userViewModel.encryptPassword(binding.passwordET.editText?.text.toString())
            val authRequest = AuthRequest(email, pswrd)
            userViewModel.logIn(authRequest)
            binding.loginBT.startAnimation()
        }

        binding.tvSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }
}