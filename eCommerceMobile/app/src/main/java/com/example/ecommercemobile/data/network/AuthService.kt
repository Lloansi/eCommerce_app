package com.example.ecommercemobile.data.network

import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthService {
    suspend fun signUp(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun logIn(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>

    suspend fun getUser(): User?
    suspend fun getUserPicture(userId: Int): Response<ResponseBody>?
    suspend fun putUserInfo(authRequest: AuthRequest): Boolean
    suspend fun putUserPicture(image: MultipartBody.Part): AuthResult<Unit>

}