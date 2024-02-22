package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.databinding.FragmentChangePasswordBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    lateinit var binding: FragmentChangePasswordBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
            userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

            binding.topAppBar.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }

            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnChangePassword.setOnClickListener {
            val encryptedCurrentPass: String
            val encryptedNewPass: String
            val currentPassET = binding.etCurrentPassword
            val newPassET = binding.etNewPassword
            val confirmNewPassET = binding.etRepeatNewPassword

            val currentPass = currentPassET.editText?.text.toString()
            val newPass = newPassET.editText?.text.toString()
            val confirmNewPass = confirmNewPassET.editText?.text.toString()

            binding.btnChangePassword.startAnimation()

            if(userViewModel.checkPasswordSyntax(newPassET, newPass)
                && (userViewModel.confirmPassword(confirmNewPassET, newPass, confirmNewPass))){
                encryptedCurrentPass = userViewModel.encryptPassword(currentPass)
                encryptedNewPass = userViewModel.encryptPassword(newPass)
                // Map the old password and the new password
                val map = mapOf<String, String>("CurrentPassword" to encryptedCurrentPass,"NewPassword" to encryptedNewPass)
                userViewModel.setNewPassword(map)

            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                binding.btnChangePassword.revertAnimation()
            }
        }
        userViewModel.isPasswordChanged.observe(viewLifecycleOwner){
            binding.btnChangePassword.revertAnimation()
            val toProfile = ChangePasswordFragmentDirections.actionChangePasswordFragmentToProfileFragment()
            findNavController().navigate(toProfile)
        }

    }
}