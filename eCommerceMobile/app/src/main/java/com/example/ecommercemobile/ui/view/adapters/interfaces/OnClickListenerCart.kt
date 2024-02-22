package com.example.ecommercemobile.ui.view.adapters.interfaces

import com.example.ecommercemobile.data.model.Product

interface OnClickListenerCart {
    fun onClick(product: Product)
    fun onRemove(product: Product)
    fun onAdd(product: Product)
    fun onDelete(product: Product)
}