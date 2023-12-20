package com.example.ecommercemobile.data.network

import com.example.ecommercemobile.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiProducts {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>
    @GET("/products/{id}")
    suspend fun getProductById(@Path("id") url: String): Response<Product>
    @GET("/products/cart/")
    suspend fun getProductsById(@Query("idList") idList:List<String>): Response<List<Product>>
}