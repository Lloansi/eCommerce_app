package com.example.ecommercemobile.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.RepositoryProducts
import com.example.ecommercemobile.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val repositoryProducts: RepositoryProducts
): ViewModel(){

    var popularProductProducts = MutableLiveData<List<Product>?>()
    var recommendedProductProducts = MutableLiveData<List<Product>?>()
    val productsFromCart = MutableLiveData<List<Product>?>()
    var allProducts = MutableLiveData<List<Product>?>()
    val product = MutableLiveData<Product?>()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch {
            val response = repositoryProducts.getAllProducts()
            allProducts.postValue(response)
        }
    }

    fun getProductByID(id: String){
        viewModelScope.launch {
            val response = repositoryProducts.getProductById(id)
            if (response != null) {
                product.postValue(response)
            }
        }
    }

    fun getProductsByID(idList: List<String>){
        viewModelScope.launch {
            val response = repositoryProducts.getProductsById(idList)
            if (response != null) {
                productsFromCart.postValue(response)
            } else {
                productsFromCart.postValue(null)
            }
        }
    }
}