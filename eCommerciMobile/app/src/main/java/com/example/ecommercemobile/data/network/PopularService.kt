package com.example.ecommercemobile.data.network

import com.example.ecommercemobile.data.model.PopularProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PopularService(private val MongoApiClient:MongoApiClient) {

    suspend fun getAllPopularProducts(): List<PopularProduct>? {
        return withContext(Dispatchers.IO) {
            val response = MongoApiClient.getAllPopularProducts()
            response.body()
        }
    }
}