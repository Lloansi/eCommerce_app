package com.example.ecommercemobile.data

import android.content.SharedPreferences
import android.util.Log
import com.example.ecommercemobile.data.model.Cart
import com.example.ecommercemobile.data.model.Order
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.network.ApiRelation
import com.example.ecommercemobile.data.network.RelationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RelationRepository @Inject constructor(
    private val api: ApiRelation,
    private val prefs: SharedPreferences): RelationService {

    override suspend fun getOrders(userID: Int): List<Order> {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext emptyList()
                val listOfOrders = api.getOrders("Bearer $token", userID)
                listOfOrders
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                emptyList()
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                emptyList()
            }

        }
    }

    override suspend fun addOrder(order: Order): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val isOrderAdded = api.addOrder("Bearer $token", order.idUser, order)
                isOrderAdded
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                false
            }
        }
    }

    override suspend fun putOrder(userID: Int, orderID: Int, state: OrderState): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val isOrderUpdated = api.putOrder("Bearer $token", userID, orderID, state)
                isOrderUpdated
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                false
            }
        }
    }

    override suspend fun getCart(userID: Int): Cart? {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext null
                val cart = api.getCart("Bearer $token", userID)
                cart
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                null
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                null
            }
        }
    }

    override suspend fun createCart(cart: Cart): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val isCartCreated = api.createCart("Bearer $token", cart.idUser, cart)
                isCartCreated
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                false
            }
        }
    }

    override suspend fun updateCart(userID: Int, productList: List<String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val isCartUpdated = api.updateCart("Bearer $token", userID, productList)
                isCartUpdated
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                false
            }
        }
    }

    override suspend fun deleteCart(userID: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val isCartDeleted = api.deleteCart("Bearer $token", userID)
                isCartDeleted
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                false
            }
        }
    }


}