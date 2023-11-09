package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.PopularItemBinding
import com.example.ecommercemobile.data.model.PopularProduct

class AdapterPopular(private val popularProductItems: List<PopularProduct>) : RecyclerView.Adapter<AdapterPopular.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = PopularItemBinding.bind(view)
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.popular_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val popularItem = popularProductItems[position]
        with(holder){
            binding.namePopular.text = popularItem.name
            binding.categoryPopular.text = popularItem.category

            Glide.with(context)
                .load(""/*Aquí anirá la carga de la imatge de Django , si fos per postgres, seria tipus: popularItem.popularImg (on guardariem la url d'on està la imatge)*/)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.imgPopular)
        }
    }

    override fun getItemCount(): Int {
        return popularProductItems.size
    }

}