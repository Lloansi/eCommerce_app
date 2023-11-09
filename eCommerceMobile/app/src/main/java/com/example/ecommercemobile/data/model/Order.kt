package com.example.ecommercemobile.data.model

data class Order(
    val idOrder: Int,
    val idUser: Int,
    val productList: List<Product>,
    val totalPrice: Double,
    val orderDate: String,
    val state: OrderState
)
