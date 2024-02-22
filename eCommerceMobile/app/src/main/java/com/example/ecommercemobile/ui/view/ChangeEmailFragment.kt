package com.example.ecommercemobile.ui.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.databinding.FragmentChangeEmailBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEmailFragment : Fragment() {

    lateinit var binding: FragmentChangeEmailBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangeEmail.setOnClickListener {

            //LOGIN TO VALIDATE CREDENTIALS -> EDIT USER -> LOGIN AGAIN FOR PREFS
            val newEmailET = binding.etNewEmail
            val passET = binding.etPassword

            val newEmail = newEmailET.editText?.text.toString()
            val pass = passET.editText?.text.toString()
            val encryptedPass: String

            binding.btnChangeEmail.startAnimation()

            if (userViewModel.checkEmailSyntax(newEmailET, newEmail)) {
                // Encrypt the password and call the changeEmail api call
                encryptedPass = userViewModel.encryptPassword(pass)

                userViewModel.changeEmail(userViewModel.user.value!!.userEmail, newEmail, encryptedPass)

                //IF SUCCESSFUL, LOG IN AGAIN
                userViewModel.authResult.observe(viewLifecycleOwner) {
                    if (it is AuthResult.Unauthorized){
                        userViewModel.user.postValue(null)
                        binding.btnChangeEmail.revertAnimation()
                        val toLogin = ChangeEmailFragmentDirections.actionChangeEmailFragmentToLoginFragment()
                        findNavController().navigate(toLogin)
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                binding.btnChangeEmail.revertAnimation()
            }
        }
    }
}