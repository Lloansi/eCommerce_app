package com.example.ecommercemobile.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentFilterOrdersBinding
import com.example.ecommercemobile.ui.view.adapters.OrdersAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerOrder
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterOrdersFragment(val filter: String) : Fragment(), OnClickListenerOrder {

    lateinit var binding: FragmentFilterOrdersBinding
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

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userID = userViewModel.user.value?.userID
        orderViewModel.vmOrderProductsMap.observe(viewLifecycleOwner) { mapOfOrdersAndProducts ->
            when (filter) {
                "COMPLETED" -> {
                    setUpRecyclerView(mapOfOrdersAndProducts.filter { it.key.state == OrderState.COMPLETED })
                }
                "CANCELLED" -> {
                    setUpRecyclerView(mapOfOrdersAndProducts.filter { it.key.state == OrderState.CANCELLED })
                }
                else -> {
                    setUpRecyclerView(mapOfOrdersAndProducts)
                }
            }
        }

        binding.swipelayout.setColorSchemeColors(R.color.white, R.color.orange)
        binding.swipelayout.setOnRefreshListener {
            binding.swipelayout.isRefreshing = false
           // binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.ordersRecyclerView.visibility = View.INVISIBLE
            orderViewModel.getOrders(userID!!)
            binding.swipelayout.isEnabled = false
        }
    }

    private fun setUpRecyclerView(mapOfOrdersAndProducts: Map<OrderClient, List<Product>>) {
        orderAdapter = OrdersAdapter(mapOfOrdersAndProducts, this)
        linearLayoutManager = LinearLayoutManager(context)
        binding.ordersRecyclerView.visibility = View.VISIBLE
        binding.swipelayout.isEnabled = true
        binding.ordersRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = orderAdapter
        }
    }

    override fun onOrderClick(orderClient: OrderClient) {
        val toOrderDetail = OrdersFragmentDirections.actionOrdersFragmentToOrderDetailFragment(orderClient.idOrder)
        findNavController().navigate(toOrderDetail)
    }
}