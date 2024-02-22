package com.example.ecommercemobile.data.network.orders

import com.example.ecommercemobile.data.model.Cart
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState

interface OrdersService {
    suspend fun getOrders(userID: Int): List<OrderClient>
    suspend fun addOrder(orderClient: OrderClient): Boolean
    suspend fun putOrder(userID: Int, orderID: Int, state: OrderState): Boolean
    suspend fun getCart(userID: Int): Cart?
    suspend fun createCart(cart: Cart): Boolean
    suspend fun updateCart(userID: Int, productList: List<String>): Boolean
    suspend fun deleteCart(userID: Int): Boolean
}