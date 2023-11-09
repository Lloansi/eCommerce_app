package com.example.ecommercemobile.data.model.auth

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequest (
    val email: String,
    val password: String
)