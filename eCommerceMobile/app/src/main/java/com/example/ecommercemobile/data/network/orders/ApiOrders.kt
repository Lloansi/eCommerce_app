package com.example.ecommercemobile.data.network.orders

import com.example.ecommercemobile.data.model.Cart
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiOrders {
    @GET("order/{userID}")
    suspend fun getOrders(@Header("Authorization") token: String,  @Path("userID") userID: Int): List<OrderClient>
    @POST("order/{userID}")
    suspend fun addOrder(@Header("Authorization") token: String, @Path("userID") userID: Int, @Body orderClient: OrderClient): Boolean
    @PUT("order/{userID}/{orderID}")
    suspend fun putOrder(@Header("Authorization") token: String, @Path("userID") userID:Int,  @Path("orderID") orderID: Int, @Body state: OrderState): Boolean
    @GET("cart/{userID}")
    suspend fun getCart(@Header("Authorization") token: String, @Path("userID") userID: Int): Cart?
    @POST("cart/{userID}")
    suspend fun createCart(@Header("Authorization") token: String, @Path("userID") userID: Int, @Body cart: Cart): Boolean
    @PUT("cart/{userID}")
    suspend fun updateCart(@Header("Authorization") token: String, @Path("userID") userID: Int, @Body productList: List<String>): Boolean
    @DELETE("cart/{userID}")
    suspend fun deleteCart(@Header("Authorization") token: String, @Path("userID") userID: Int): Boolean
}