package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.databinding.FragmentLoginBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            println("User del login: $user")
            if(user!=null) {
                userViewModel.getUserImage(user.userID)
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
            binding.loginBT.revertAnimation()

        }

        userViewModel.authenticate()

        userViewModel.authResult.observe(viewLifecycleOwner) {
            when (it){
                is AuthResult.Authorized -> {
                    userViewModel.getUser()
                }
                is AuthResult.Unauthorized -> {
                    println("Unauthorized")
                    binding.loginBT.revertAnimation()
                }
                is AuthResult.UnknownError -> {
                    println("UnknownError")
                    binding.loginBT.revertAnimation()
                }
                null -> println("UnknownError")
            }
        }

        binding.loginBT.setOnClickListener {
            val email = binding.emailET.editText?.text.toString()
            val pswrd =  userViewModel.encryptPassword(binding.passwordET.editText?.text.toString())

            val authRequest = AuthRequest(email, pswrd)
            userViewModel.logIn(authRequest)
            binding.loginBT.startAnimation()
        }

        binding.signup2TV.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

}