package com.example.ecommercemobile.utils

import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.data.model.Specs

object TempProvider {
    val product1 = Product("1","Minecraft Server","imagen", "Server", 9.99,
        "Spain", Specs("Intel i7","16gb","4TB",true), 10, 0)
    val product2 = Product("2","Music","imagen", "Music", 29.99,
        "Spain", Specs("Intel i7","16gb","4TB",true), 10, 0)
    val product3 = Product("3","Discord Server","imagen", "Chat", 19.99,
        "Spain", Specs("Intel i7","16gb","4TB",true), 10,0)
    val product4 = Product("4","CSGO Server","imagen", "Server", 19.99,
        "Spain", Specs("Intel i7","16gb","4TB",true), 10,0)
    val productList = mutableListOf<Product>(
        product1, product2, product3, product4, product1
    )
    val productList2 = mutableListOf<Product>(product4)

    val orderClient1 = OrderClient(1,1,productList.shuffled().map { it.id }, productList.sumOf { it.price }, "2023-05-05", OrderState.COMPLETED)
    val orderClient2 = OrderClient(2,1,productList.shuffled().map { it.id }, productList.sumOf { it.price }, "2023-10-08", OrderState.CANCELLED)
    val orderClient3 = OrderClient(3,1,productList2.map { it.id }, productList2.sumOf { it.price }, "2023-10-08", OrderState.COMPLETED)
    val orderClientListTest = mutableListOf<OrderClient>(
        orderClient1, orderClient2, orderClient3
    )
}