package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.RelationRepository
import com.example.ecommercemobile.data.model.Order
import com.example.ecommercemobile.data.model.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: RelationRepository): ViewModel() {

    var vmOrderList = MutableLiveData<List<Order>>()

    // Gets the orders of the user or an empty list if there's no orders
    fun getOrders(userID: Int) {
        viewModelScope.launch {
            val result = repository.getOrders(userID)
            vmOrderList.postValue(result)
        }
    }

    // Adds an order a calls getOrders to update the orderList
   fun addOrder(order: Order) {
        viewModelScope.launch {
            if (vmOrderList.value != null){
                val result = repository.addOrder(order)
                if (result) {
                    getOrders(order.idUser)
                }
            }
        }
    }

    // This function is called automatically when payment is made
    fun putOrder(userID: Int, orderID: Int) {
        viewModelScope.launch {
            if (vmOrderList.value != null){
                val result = repository.putOrder(userID, orderID, OrderState.COMPLETED)
                if (result) {
                    getOrders(userID)
                }
            }
        }
    }
}