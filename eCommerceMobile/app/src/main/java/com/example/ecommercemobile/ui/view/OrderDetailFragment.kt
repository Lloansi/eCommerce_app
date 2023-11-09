package com.example.ecommercemobile.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentOrderDetailBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.view.adapters.OrderProductsAdapter
import com.example.ecommercemobile.utils.ExtensionFunctions.round
import com.example.ecommercemobile.utils.TempProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment(), OnClickListenerProduct {
    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var orderProductsAdapter: OrderProductsAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val orderID = arguments?.getInt("orderID")!!
        binding.dateOrderedTV.text = "Date purchased: ${TempProvider.orderListTest.find { it.idOrder == orderID }!!.orderDate}"
        binding.orderNumberTV.text = "Order Nº: ${orderID.toString()}"
        binding.totalPriceTV.text = "${TempProvider.orderListTest.find { it.idOrder == orderID }!!.totalPrice.round(2)}€"
        binding.stateTV.text = TempProvider.orderListTest.find { it.idOrder == orderID }!!.state.name.capitalize(
            Locale.ROOT)
        setUpRecyclerView(TempProvider.orderListTest.find { it.idOrder == orderID }!!.productList)

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
        // Irá al detalle del producto
        println(product.name)
    }


}