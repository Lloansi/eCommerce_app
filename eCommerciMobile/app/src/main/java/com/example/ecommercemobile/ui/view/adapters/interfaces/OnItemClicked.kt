package com.example.ecommercemobile.ui.view.adapters.interfaces

import com.example.ecommercemobile.data.model.PopularProduct
import com.example.ecommercemobile.data.model.RecommendedProduct

interface OnItemClicked {

    fun onPopularItemClicked(popularProduct: PopularProduct)
    fun onRecommendedItemClicked(recommendedProduct: RecommendedProduct)
}