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
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentOrderDetailBinding
import com.example.ecommercemobile.ui.view.adapters.OrderProductsAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.example.ecommercemobile.utils.ExtensionFunctions.round
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment(), OnClickListenerProduct {

    lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderProductsAdapter: OrderProductsAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var orderViewModel: OrdersViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderViewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val orderID = arguments?.getInt("orderID")!!
        val mapOrdersAndProducts = orderViewModel.vmOrderProductsMap.value!!
        val order = mapOrdersAndProducts.filter { it.key.idOrder == orderID }.keys.first()

        setUpRecyclerView(mapOrdersAndProducts[order]!!)

        binding.dateOrderedTV.text = "Date purchased: ${order.orderDate.split("T")[0]}"
        binding.orderNumberTV.text = "Order Nº: $orderID"
        binding.totalPriceTV.text = "${order.totalPrice.round(2)}€"
        binding.stateTV.text = order.state.name.capitalize(Locale.ROOT)
        binding.emailTV.text = userViewModel.user.value?.userEmail
    }

    private fun setUpRecyclerView(productList: List<Product>) {
        orderProductsAdapter = OrderProductsAdapter(productList, this)
        linearLayoutManager = LinearLayoutManager(context)
        binding.orderProductsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = orderProductsAdapter
        }
    }

    override fun onClickProduct(product: Product) {
        val toProductDetail = OrderDetailFragmentDirections.actionOrderDetailFragmentToDetailFragment(product.id)
        findNavController().navigate(toProductDetail)
    }
}