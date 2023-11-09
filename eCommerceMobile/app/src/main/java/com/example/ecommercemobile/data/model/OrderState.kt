package com.example.ecommercemobile.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderState{
    COMPLETED, ONGOING, CANCELLED
}