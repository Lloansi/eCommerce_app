package com.example.ecommercemobile.data.network.auth

import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

interface AuthService {
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun logIn(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun signUp(authRequest: AuthRequest): AuthResult<Unit>
    suspend fun getUser(): User?
    suspend fun getUserPicture(userId: Int): Response<ResponseBody>?
    suspend fun putUserInfo(authRequest: AuthRequest): Boolean
    suspend fun putUserPicture(image: MultipartBody.Part): AuthResult<Unit>
    suspend fun verifyPassword(password:String):Boolean
    suspend fun setNewPassword(map: Map<String, String>):Boolean
    suspend fun resetPasswordEmail(email: String): Boolean
    suspend fun changeEmail(oldMail:String, newMail:String, password: String):Boolean
    suspend fun validateEmail(email: String): Boolean
}