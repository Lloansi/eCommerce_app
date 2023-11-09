package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.databinding.LogoutModalBottomSheetBinding
import com.example.ecommercemobile.ui.view.ProfileFragmentDirections
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.runBlocking

class LogOutBottomSheet: BottomSheetDialogFragment() {
    private lateinit var binding: LogoutModalBottomSheetBinding
    lateinit var userViewModel: UserViewModel
    lateinit var prefs: SharedPreferences
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = LogoutModalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.authResult.observe(viewLifecycleOwner) {
            if (it is AuthResult.Unauthorized){
                binding.logoutBT.revertAnimation()
                val toLogin = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                findNavController().navigate(toLogin)
                dismiss()
            }
        }


        binding.cancelBT.setOnClickListener {
            dismiss()
        }
        binding.logoutBT.setOnClickListener{
            binding.logoutBT.startAnimation()
            prefs =
                requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
            prefs.edit()
                .putString("jwt", null)
                .apply()
            runBlocking {
                userViewModel.authenticate()
                userViewModel.user.postValue(null)
            }
        }
    }

    companion object {
        const val TAG = "LogOutBottomSheet"
    }
}