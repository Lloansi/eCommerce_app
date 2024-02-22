package com.example.ecommercemobile.ui.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercemobile.databinding.ForgotPasswordPopupBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel

class ForgetPassDialog: DialogFragment() {

    lateinit var binding: ForgotPasswordPopupBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = ForgotPasswordPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailSentTV.visibility =  View.INVISIBLE

        binding.resetPassBTN.setOnClickListener {
            val email = binding.emailET.editText!!.text.toString()
            if (userViewModel.checkEmailSyntax(binding.emailET, email)){
                userViewModel.resetPassword(email)
                binding.emailSentTV.visibility =  View.VISIBLE
            }
        }
    }

    companion object {
        const val TAG = "ForgetPasswordDialog"
    }
}