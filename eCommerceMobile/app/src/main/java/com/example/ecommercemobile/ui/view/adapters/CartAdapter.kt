package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.ItemCartBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerCart

class CartAdapter(private val productList: List<Product>,
                  val listener: OnClickListenerCart
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCartBinding.bind(view)


        fun setListener(product: Product) {
            binding.root.setOnClickListener {
                listener.onClick(product)
            }
            binding.addBT.setOnClickListener {
                listener.onAdd(product)
            }
            binding.removeBT.setOnClickListener {
                listener.onRemove(product)
            }
            binding.deleteBT.setOnClickListener {
                listener.onDelete(product)
            }
        }
    }
    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        context = parent.context
        val view = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        with(holder) {
            setListener(product)
            binding.productTV.text = product.name
            binding.priceTV.text = "${product.price}${context.getString(R.string.euro)}"
            binding.categoryChip.text = product.category
            binding.quantityET.setText(productList.count { it.idProduct == product.idProduct }.toString())
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateProducts() {
        notifyDataSetChanged()
    }

}