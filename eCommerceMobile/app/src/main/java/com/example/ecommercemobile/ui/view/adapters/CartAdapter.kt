package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.ItemCartBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerCart
import com.example.ecommercemobile.utils.Constants

class CartAdapter(private val productMap: Map<Product?,Int>,
                  val listener: OnClickListenerCart
): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private lateinit var context: Context

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        context = parent.context
        val view = ItemCartBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(view.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productMap.keys.elementAt(position)
        with(holder) {
            setListener(product!!)
            binding.productTV.text = product.name
            binding.productTV.isSelected = true
            binding.priceTV.text = "${product.price}${context.getString(R.string.euro)}"
            binding.categoryChip.text = product.category
            binding.quantityET.setText(productMap[product].toString())
            Glide.with(context)
                .load("${Constants.PRODUCT_API_URL}products/images/${product.image}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.ivProduct)
        }
    }

    override fun getItemCount(): Int {
        return productMap.size
    }

    fun updateProducts() {
        notifyDataSetChanged()
    }
}