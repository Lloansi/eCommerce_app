package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.Repository
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.databinding.FragmentSignUpBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpBT.setOnClickListener {


            if (userViewModel.validateEmail(binding.emailET, binding.emailET.editText!!.text.toString()) &&
                userViewModel.validatePassword(binding.passwordET, binding.passwordET.editText!!.text.toString()) &&
                (userViewModel.confirmPassword(binding.repeatPassET,binding.passwordET.editText!!.text.toString(), binding.repeatPassET.editText!!.text.toString() ))){

                val encryptedPass = userViewModel.encryptPassword(binding.passwordET.editText!!.text.toString())
                userViewModel.signUp(AuthRequest(
                    binding.emailET.editText!!.text.toString(),
                    encryptedPass
                ))

                userViewModel.validateEmail(binding.emailET.editText!!.text.toString())

                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                findNavController().navigate(action)

            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }



        binding.signin2TV.setOnClickListener{
            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

}