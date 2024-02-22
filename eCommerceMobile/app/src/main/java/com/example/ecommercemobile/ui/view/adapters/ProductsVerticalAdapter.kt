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
import com.example.ecommercemobile.databinding.ItemProductBinding
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.utils.Constants

class ProductsVerticalAdapter(
    private val productList: List<Product>,
    val listener : OnClickListenerProduct
) : RecyclerView.Adapter<ProductsVerticalAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemProductBinding.bind(view)
        fun setListener(product: Product) {
            binding.root.setOnClickListener {
                listener.onClickProduct(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        with(holder){
            setListener(product)
            binding.namePopular.text = product.name
            binding.namePopular.isSelected = true
            binding.categoryPopular.text = product.category
            binding.locationPopular.text = product.location
            binding.pricePopular.text = product.price.toString()

            Glide.with(context)
                .load("${Constants.PRODUCT_API_URL}products/images/${product.image}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.imgPopular)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}