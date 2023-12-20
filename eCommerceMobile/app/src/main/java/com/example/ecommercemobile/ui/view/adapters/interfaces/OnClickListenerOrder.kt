package com.example.ecommercemobile.ui.view.adapters.interfaces

import com.example.ecommercemobile.data.model.OrderClient

interface OnClickListenerOrder {
    fun onOrderClick(orderClient: OrderClient)
}