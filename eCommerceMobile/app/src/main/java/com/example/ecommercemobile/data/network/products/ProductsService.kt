package com.example.ecommercemobile.data.network.products

import com.example.ecommercemobile.data.model.Product


interface ProductsService {
    suspend fun getAllProducts(): List<Product>?
    suspend fun getProductById(id: String): Product?
    suspend fun getProductsById(idList:List<String>): List<Product>?
    suspend fun updateTimesBought(idList:List<String>): List<Product>?
}