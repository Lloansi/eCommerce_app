package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.ItemOrdersBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerOrder
import com.example.ecommercemobile.utils.Constants
import com.example.ecommercemobile.utils.ExtensionFunctions.round

class OrdersAdapter(private val mapOfOrdersAndProducts: Map<OrderClient, List<Product>>,
                    val listener: OnClickListenerOrder,
   ): RecyclerView.Adapter<OrdersAdapter.ViewHolder>(){

    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrdersBinding.bind(view)
        fun setListener(orderClient: OrderClient) {
            binding.root.setOnClickListener {
                listener.onOrderClick(orderClient)
            }
            binding.cardview2.setOnClickListener {
                listener.onOrderClick(orderClient)
            }
            binding.cardview1.setOnClickListener {
                listener.onOrderClick(orderClient)
            }
            binding.seemoreBT.setOnClickListener {
                listener.onOrderClick(orderClient)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        context = parent.context
        val view = ItemOrdersBinding.inflate(android.view.LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {
        val order = mapOfOrdersAndProducts.keys.elementAt(position)
        val products = mapOfOrdersAndProducts[order]
        with(holder) {
            setListener(order)
            binding.dateOrderedTV.text = " ${order.orderDate.split("T")[0]}"
            binding.totalPriceTV.text = order.totalPrice.round(2).toString()+"€"
            binding.orderNumberTV.text = order.idOrder.toString()
            if (!products.isNullOrEmpty()) {
                binding.productTV1.text = products[0].name
                binding.productTV1.isSelected = true
                binding.priceTV1.text = products[0].price.round(2).toString()+"€"
                binding.categoryChip1.text = products[0].category
                Glide.with(context)
                    .load("${Constants.PRODUCT_API_URL}products/images/${products[0].image}")
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.ivProduct1)
                if (order.productList.size != 1){
                    binding.productTV2.text = products[1].name
                    binding.productTV2.isSelected = true
                    binding.priceTV2.text = products[1].price.round(2).toString()+"€"
                    binding.categoryChip2.text = products[1].category
                    Glide.with(context)
                        .load("${Constants.PRODUCT_API_URL}products/images/images/${products[1].image}")
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .circleCrop()
                        .into(binding.ivProduct2)
                } else {
                    binding.productTV2.visibility = View.GONE
                    binding.priceTV2.visibility = View.GONE
                    binding.categoryChip2.visibility = View.GONE
                    binding.ivProduct2.visibility = View.GONE
                    binding.cardview2.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mapOfOrdersAndProducts.size
    }
}