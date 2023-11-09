package com.example.ecommercemobile.data.network

import com.example.ecommercemobile.data.model.PopularProduct
import com.example.ecommercemobile.data.model.RecommendedProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecommendedService(private val MongoApiClient:MongoApiClient) {

    suspend fun getAllRecommendedProducts(): List<RecommendedProduct>? {
        return withContext(Dispatchers.IO) {
            val response = MongoApiClient.getAllRecommendedProducts()
            response.body()
        }
    }
}
