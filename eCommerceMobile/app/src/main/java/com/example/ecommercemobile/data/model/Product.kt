package com.example.ecommercemobile.data.model


data class Product(
    val id: String,
    var name: String,
    var image: String,
    var category: String,
    var price: Double,
    var location: String,
    var specs: Specs?,
    var stock: Int,
    var timesBought: Int
)
