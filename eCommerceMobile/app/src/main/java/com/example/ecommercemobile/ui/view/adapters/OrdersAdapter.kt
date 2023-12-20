package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.ItemOrdersBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerOrder
import com.example.ecommercemobile.utils.ExtensionFunctions.round

class OrdersAdapter(private val orderClientList: List<OrderClient>,
                    private val productList: List<Product>,
                    val listener: OnClickListenerOrder,
   ): RecyclerView.Adapter<OrdersAdapter.ViewHolder>(){

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrdersBinding.bind(view)


        fun setListener(orderClient: OrderClient) {
            binding.root.setOnClickListener {
                listener.onOrderClick(orderClient)
            }
        }
    }
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        context = parent.context
        val view = ItemOrdersBinding.inflate(android.view.LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {
        val order = orderClientList[position]

        with(holder) {
            setListener(order)
            binding.dateOrderedTV.text = order.orderDate
            binding.totalPriceTV.text = order.totalPrice.round(2).toString()+"€"
            binding.orderNumberTV.text = order.idOrder.toString()
            // faltan las fotos
            binding.product1TV.text = productList[0].name
            binding.price1TV.text = productList[0].price.round(2).toString()+"€"
            binding.categoryChip.text = productList[0].category
            if (order.productList.size != 1){
                binding.product2TV.text = productList[1].name
                binding.price2TV.text = productList[1].price.round(2).toString()+"€"
                binding.category2Chip.text = productList[1].category
            } else {
                binding.product2TV.visibility = View.GONE
                binding.price2TV.visibility = View.GONE
                binding.category2Chip.visibility = View.GONE
                binding.product2IV.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int {
        return orderClientList.size
    }

}