package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.RepositoryProducts
import com.example.ecommercemobile.data.model.PopularProduct
import com.example.ecommercemobile.data.model.RecommendedProduct
import kotlinx.coroutines.launch


class ProductsViewModel(private val repositoryProducts: RepositoryProducts): ViewModel(){

    var popularProductProducts = MutableLiveData<List<PopularProduct>?>()
    var recommendedProductProducts = MutableLiveData<List<RecommendedProduct>?>()

    init {
        getAllPopularProducts()
        getAllRecommendedProducts()
    }

    //POPULAR
    fun getAllPopularProducts() {
        viewModelScope.launch {
            popularProductProducts.value = repositoryProducts.getAllPopularProducts()
        }
    }

    //RECOMMENDED
    fun getAllRecommendedProducts() {
        viewModelScope.launch {
            recommendedProductProducts.value = repositoryProducts.getAllRecommendedProducts()
        }
    }


}