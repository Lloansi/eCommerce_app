package com.example.ecommercemobile.data.model.auth

sealed class AuthResult<T>(val data: T? = null) {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>: AuthResult<T>()
    class UnknownError<T>: AuthResult<T>()
    class NoConnection<T>: AuthResult<T>()
    class ServerUnavailable<T>: AuthResult<T>()

}