package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.data.model.Order
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.databinding.FragmentFilterOrdersBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerOrder
import com.example.ecommercemobile.ui.view.adapters.OrdersAdapter
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterOrdersFragment(val filter: String) : Fragment(), OnClickListenerOrder {
    private lateinit var binding: FragmentFilterOrdersBinding
    private lateinit var orderAdapter: OrdersAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var userViewModel: UserViewModel
    private lateinit var orderViewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        orderViewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        binding = FragmentFilterOrdersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderViewModel.vmOrderList.observe(viewLifecycleOwner){ orderList ->
            // Filtrar la lista de ordenes que estará en el viewmodel
            when (filter){
                "COMPLETED" -> {
                    setUpRecyclerView(orderList.filter { it.state == OrderState.COMPLETED })
                }
                "CANCELLED" -> {
                    setUpRecyclerView(orderList.filter { it.state == OrderState.CANCELLED })
                }
                else -> {
                    setUpRecyclerView(orderList)
                }
            }
        }

    }

    private fun setUpRecyclerView(orderList: List<Order>) {
        orderAdapter = OrdersAdapter(orderList, this)
        linearLayoutManager = LinearLayoutManager(context)
        binding.ordersRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = orderAdapter
        }
    }

    override fun onOrderClick(order: Order) {
        val toOrderDetail = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment()
        findNavController().navigate(toOrderDetail)
    }

}