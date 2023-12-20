package com.example.ecommercemobile.data.model

data class OrderClient(
    val idOrder: Int,
    val idUser: Int,
    val productList: List<String>,
    val totalPrice: Double,
    val orderDate: String,
    val state: OrderState
)
