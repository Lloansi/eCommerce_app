package com.example.ecommercemobile.data

import android.content.SharedPreferences
import android.util.Log
import com.example.ecommercemobile.data.model.Cart
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.network.ApiRelation
import com.example.ecommercemobile.data.network.RelationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RelationRepository @Inject constructor(
    private val api: ApiRelation,
    private val prefs: SharedPreferences
) : RelationService {


    private suspend fun <R> make(ifNot: R, exec: suspend () -> R): R {
        return withContext(Dispatchers.IO) {
            try {
                exec()
            } catch (e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                ifNot
            } catch (e: Exception) {
                e.message?.let { Log.e("Error " + e.cause, it) }
                ifNot
            }

        }
    }


    override suspend fun getOrders(userID: Int): List<OrderClient> {
        return make(emptyList()) {
            val token = prefs.getString("jwt", null)!!
            val listOfOrders = api.getOrders("Bearer $token", userID)
            listOfOrders
        }
    }

     override suspend fun addOrder(orderClient: OrderClient): Boolean {
        return make(false) {
                val token = prefs.getString("jwt", null)!!
                val isOrderAdded = api.addOrder("Bearer $token", orderClient.idUser, orderClient)
                isOrderAdded

        }
    }


    override suspend fun putOrder(userID: Int, orderID: Int, state: OrderState): Boolean {
        return make(false) {
                val token = prefs.getString("jwt", null)!!
                val isOrderUpdated = api.putOrder("Bearer $token", userID, orderID, state)
                isOrderUpdated
        }
    }

    override suspend fun getCart(userID: Int): Cart? {
        return make(null) {
                val token = prefs.getString("jwt", null)!!
                val cart = api.getCart("Bearer $token", userID)
                cart

        }
    }

    override suspend fun createCart(cart: Cart): Boolean {
        return make(false) {
                val token = prefs.getString("jwt", null)!!
                val isCartCreated = api.createCart("Bearer $token", cart.idUser, cart)
                isCartCreated

        }
    }

    override suspend fun updateCart(userID: Int, productList: List<String>): Boolean {
        return make(false) {
                prefs.getString("jwt", null)?.let {
                    api.updateCart("Bearer $it", userID, productList)
                } ?: false
        }
    }

    override suspend fun deleteCart(userID: Int): Boolean {
        return make(false) {
                prefs.getString("jwt", null)?.let {
                    api.deleteCart("Bearer $it", userID)
                } ?: false
        }
    }


}