package com.example.ecommercemobile.data.model

data class Cart(
    var idCart: Int,
    var idUser: Int,
    var productList: List<Product>
)
