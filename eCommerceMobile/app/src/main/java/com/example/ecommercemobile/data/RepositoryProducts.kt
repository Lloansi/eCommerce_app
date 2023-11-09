package com.example.ecommercemobile.data

import com.example.ecommercemobile.data.model.PopularProduct
import com.example.ecommercemobile.data.model.RecommendedProduct
import com.example.ecommercemobile.data.network.PopularService
import com.example.ecommercemobile.data.network.RecommendedService

class RepositoryProducts (private val popularProducts: PopularService, private val recommendedProducts: RecommendedService) {

    //POPULAR
    suspend fun getAllPopularProducts(): List<PopularProduct>?{
        val response =popularProducts.getAllPopularProducts()
        return response
    }

    suspend fun getPopularProductById(): PopularProduct{
        TODO()
    }

    suspend fun addPopularProduct(): Boolean?{
        TODO()
    }

    suspend fun deletePopularProduct(): Boolean?{
        TODO()
    }

    suspend fun updatePopularProduct(): Boolean?{
        TODO()
    }

    //RECOMMENDED
    suspend fun getAllRecommendedProducts(): List<RecommendedProduct>?{
        val response = recommendedProducts.getAllRecommendedProducts()
        return response
    }

    suspend fun getRecommendedProductById(): RecommendedProduct{
        TODO()
    }

    suspend fun addRecommendedProduct(): Boolean?{
        TODO()
    }

    suspend fun deleteRecommendedProduct(): Boolean?{
        TODO()
    }

    suspend fun updateRecommendedProduct(): Boolean?{
        TODO()
    }


}