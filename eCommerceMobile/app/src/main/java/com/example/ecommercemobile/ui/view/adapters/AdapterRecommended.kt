package com.example.ecommercemobile.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.RecommendedItemBinding
import com.example.ecommercemobile.data.model.RecommendedProduct

class AdapterRecommended(private val recommendItems: List<RecommendedProduct>) :  RecyclerView.Adapter<AdapterRecommended.ViewHolder>()  {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = RecommendedItemBinding.bind(view)
    }

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.recommended_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterRecommended.ViewHolder, position: Int) {
        val popularItem = recommendItems[position]
        with(holder){
            binding.nameProductRecommended.text = popularItem.name
            binding.categoryRecommended.text = popularItem.category

            Glide.with(context)
                .load(""/*Aquí anirá la carga de la imatge de Django , si fos per postgres, seria tipus: recommendeItems.recommendedImg (on guardariem la url d'on està la imatge)*/)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.imgRecommended)
        }
    }

    override fun getItemCount(): Int {
        return recommendItems.size
    }


}