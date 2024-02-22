package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.repository.OrdersRepository
import com.example.ecommercemobile.data.repository.ProductsRepository
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val repository: OrdersRepository
): ViewModel() {

    var vmOrderListClient = MutableLiveData<List<OrderClient>>()
    var vmOrderProductsMap = MutableLiveData<Map<OrderClient, List<Product>>>()

    // Create a map of orders and their products
    fun mapOfOrdersAndProducts(){
        viewModelScope.launch {
            val map:MutableMap<OrderClient, List<Product>> = mutableMapOf()
            for (order in vmOrderListClient.value!!) {
                map[order] = productsRepository.getProductsById(order.productList)!!
            }
            vmOrderProductsMap.postValue(map)
        }
    }

    // Gets the orders of the user or an empty list if there's no orders
    fun getOrders(userID: Int) {
        viewModelScope.launch {
            val result = repository.getOrders(userID)
            vmOrderListClient.postValue(result)
        }
    }

    // Adds an order a calls getOrders to update the orderList
   fun addOrder(orderClient: OrderClient) {
        viewModelScope.launch {
            val result = repository.addOrder(orderClient)
            if (result) {
                getOrders(orderClient.idUser)
            }
        }
    }

    // This function is called automatically when payment is made
    fun putOrder(userID: Int, orderID: Int) {
        viewModelScope.launch {
            if (vmOrderListClient.value != null){
                val result = repository.putOrder(userID, orderID, OrderState.COMPLETED)
                if (result) {
                    getOrders(userID)
                }
            }
        }
    }
}