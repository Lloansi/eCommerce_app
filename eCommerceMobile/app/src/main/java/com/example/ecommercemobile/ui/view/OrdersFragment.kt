package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ecommercemobile.databinding.FragmentOrdersBinding
import com.example.ecommercemobile.ui.view.adapters.OrdersViewPagerAdapter
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    lateinit var binding: FragmentOrdersBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var orderViewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        orderViewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        binding = FragmentOrdersBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val userID = userViewModel.user.value?.userID
        orderViewModel.getOrders(userID!!)
        orderViewModel.vmOrderListClient.observe(viewLifecycleOwner){ orderList ->
            orderViewModel.mapOfOrdersAndProducts()
        }

        // Same fragment but different data in the recyclerView
        val orderCategories = arrayListOf<Fragment>(
            FilterOrdersFragment("ALL"),
            FilterOrdersFragment("COMPLETED"),
            FilterOrdersFragment("CANCELLED")
        )
        val viewPagerAdapter = OrdersViewPagerAdapter(
            childFragmentManager,
            lifecycle,
            orderCategories,
        )
        binding.viewPagerOrders.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerOrders){tab, position ->
            when(position){
                0 -> tab.text = "All"
                1 -> tab.text = "Completed"
                2 -> tab.text = "Cancelled"
            }
        }.attach()
    }
}