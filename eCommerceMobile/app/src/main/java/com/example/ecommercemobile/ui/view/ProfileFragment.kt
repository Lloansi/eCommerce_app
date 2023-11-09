package com.example.ecommercemobile.ui.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.FragmentProfileBinding
import com.example.ecommercemobile.ui.view.adapters.LogOutBottomSheet
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
   private lateinit var binding: FragmentProfileBinding
    lateinit var userViewModel: UserViewModel
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.usernameTV.text = userViewModel.user.value?.userEmail
        userViewModel.userImage.observe(viewLifecycleOwner){ userImage ->
            if (userImage != null){
                binding.profileIV.setImageBitmap(userImage)
            } else binding.profileIV.setImageResource(R.drawable.placeholder)

        }
        userViewModel.user.observe(viewLifecycleOwner){ user ->
            if (user!=null){
                binding.usernameTV.text = user.userEmail
            }
        }
        binding.logoutLY.setOnClickListener {
            val modalBottomSheet = LogOutBottomSheet()
            modalBottomSheet.show(requireParentFragment().parentFragmentManager, LogOutBottomSheet.TAG)
        }
        binding.editProfileLY.setOnClickListener {
            val toEditProfile = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(toEditProfile)
        }
        binding.ordersLY.setOnClickListener {
            val toOrders = ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
            findNavController().navigate(toOrders)
        }


    }


}