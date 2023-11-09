package com.example.ecommercemobile.utils

import com.example.ecommercemobile.data.model.Cart
import com.example.ecommercemobile.data.model.Location
import com.example.ecommercemobile.data.model.Order
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.data.model.Specs

object TempProvider {
    val product1 = Product(1,"Minecraft Server","imagen", "Server", 9.99,
        Location(2.5,2.4), Specs("Intel i7",1,100,true), 10)
    val product2 = Product(2,"Music","imagen", "Music", 29.99,
        Location(2.5,2.4), Specs("Intel i7",1,100,true), 10)
    val product3 = Product(3,"Discord Server","imagen", "Chat", 19.99,
        Location(2.5,2.4), Specs("Intel i7",1,100,true), 10)
    val product4 = Product(4,"CSGO Server","imagen", "Server", 19.99,
        Location(2.5,2.4), Specs("Intel i7",1,100,true), 10)
    val productList = mutableListOf<Product>(
        product1, product2, product3, product4, product1
    )
    val productList2 = mutableListOf<Product>(product4)
    val cartTest = Cart(1,1, productList)

    val order1 = Order(1,1,productList.shuffled(), productList.sumOf { it.price }, "2023-05-05", OrderState.COMPLETED)
    val order2 = Order(2,1,productList.shuffled(), productList.sumOf { it.price }, "2023-10-08", OrderState.CANCELLED)
    val order3 = Order(3,1,productList2, productList2.sumOf { it.price }, "2023-10-08", OrderState.COMPLETED)
    val orderListTest = mutableListOf<Order>(
        order1, order2, order3
    )
}