package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.repository.ProductsRepository
import com.example.ecommercemobile.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
): ViewModel(){

    val productsFromCart = MutableLiveData<List<Product>?>()
    var allProducts = MutableLiveData<List<Product>?>()
    val product = MutableLiveData<Product?>()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val response = productsRepository.getAllProducts()
            allProducts.postValue(response)
        }
    }

    fun getProductByID(id: String){
        viewModelScope.launch {
            val response = productsRepository.getProductById(id)
            if (response != null) {
                product.postValue(response)
            }
        }
    }

    fun getProductsByID(idList: List<String>){
        viewModelScope.launch {
            val response = productsRepository.getProductsById(idList)
            if (response != null) {
                productsFromCart.postValue(response)
            } else {
                productsFromCart.postValue(null)
            }
        }
    }

    fun updateTimesBought(idList: List<String>){
        viewModelScope.launch {
            val response = productsRepository.updateTimesBought(idList)
            // If the response is not null, call the getAllProducts() method to update the list of products
            if (response != null) {
                getAllProducts()
            }
        }
    }
}