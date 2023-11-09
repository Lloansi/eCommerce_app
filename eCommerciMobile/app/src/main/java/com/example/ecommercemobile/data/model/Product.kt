package com.example.ecommercemobile.data.model


data class Product(
    val idProduct: Int,
    var name: String,
    var image: String,
    var category: String,
    var price: Double,
    var location: Location,
    var specs: Specs,
    var stock: Int
)
