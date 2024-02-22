package com.example.ecommercemobile.data.repository

import android.util.Log
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.data.network.products.ApiProducts
import com.example.ecommercemobile.data.network.products.ProductsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val api: ApiProducts,
    ): ProductsService {

    override suspend fun getAllProducts(): List<Product>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getAllProducts()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                null
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                null
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            }
        }
    }

    override suspend fun getProductById(id: String): Product? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProductById(id)
                response.body()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                null
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                null
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            }
        }
    }

    override suspend fun getProductsById(idList: List<String>): List<Product>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProductsById(idList)
                response.body()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                null
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                null
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            }
        }
    }

    override suspend fun updateTimesBought(idList: List<String>): List<Product>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateTimesBought(idList)
                response.body()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                null
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                null
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                null
            }
        }
    }
}