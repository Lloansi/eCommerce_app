package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.ItemOrderProductsBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.utils.ExtensionFunctions.round

class OrderProductsAdapter(
    private val productList: List<Product>,
    val listener: OnClickListenerProduct
): RecyclerView.Adapter<OrderProductsAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemOrderProductsBinding.bind(view)


        fun setListener(product: Product) {
            binding.root.setOnClickListener {
                listener.onClickProduct(product)
            }

        }
    }

    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderProductsAdapter.ViewHolder {
        context = parent.context
        val view = ItemOrderProductsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        with(holder) {
            setListener(product)
            binding.productTV.text = product.name
            binding.priceTV.text = "${product.price.round(2)}${context.getString(R.string.euro)}"
            binding.categoryChip.text = product.category
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

}
