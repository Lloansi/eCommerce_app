package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.databinding.FragmentEditAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAccountFragment : Fragment() {

    lateinit var binding: FragmentEditAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAccountBinding.inflate(inflater, container, false)

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changeEmailLinlay.setOnClickListener {
            val toChangeEmailFragment = EditAccountFragmentDirections.actionEditAccountFragmentToChangeEmailFragment()
            findNavController().navigate(toChangeEmailFragment)
        }

        binding.changePswdLinlay.setOnClickListener {
            val toChangePasswordFragment = EditAccountFragmentDirections.actionEditAccountFragmentToChangePasswordFragment()
            findNavController().navigate(toChangePasswordFragment)
        }
    }
}