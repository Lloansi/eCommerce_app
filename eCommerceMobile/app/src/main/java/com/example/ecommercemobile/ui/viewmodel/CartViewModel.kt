package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.RelationRepository
import com.example.ecommercemobile.data.model.Cart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: RelationRepository): ViewModel() {

    var vmCart = MutableLiveData<Cart?>()

    // If the user has a cart, get it. Otherwise, create a new cart
    fun getCart(userID: Int) {
        viewModelScope.launch {
            val result = repository.getCart(userID)
            if (result!!.idCart != 0) {
                vmCart.postValue(result)
            } else {
                createCart(Cart(0, userID, emptyList()))
            }
        }
    }

    // This function is called when the user exits the app or adds a item to a cart and the viewModel Cart is null
    fun createCart(cart: Cart) {
        viewModelScope.launch {
            val result = repository.createCart(cart)
            if (result) {
                getCart(cart.idUser)
            }
        }
    }

    // This function is called when the user adds or remove an item from the cart
    fun updateCart(userID: Int, productList: List<String>) {
        viewModelScope.launch {
            if (vmCart.value != null){
                val result = repository.updateCart(userID, productList)
                if (result) {
                    getCart(userID)
                }
            }
        }
    }

    // This function is called when the user makes the purchase, the cart is deleted
    // and a new order is created. Also call the getCart function to create a new empty cart
    fun deleteCart(userID: Int) {
        viewModelScope.launch {
            if (vmCart.value != null){
                val result = repository.deleteCart(userID)
                if (result) {
                    getCart(userID)
                }
            }
        }
    }
}