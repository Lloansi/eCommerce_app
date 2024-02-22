package com.example.ecommercemobile.data.network.auth

import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.TokenResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiAuth {
    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token: String): Response<ResponseBody>
    @POST("login")
    suspend fun logIn(@Body request: AuthRequest): TokenResponse
    @POST("signup")
    suspend fun signUp(@Body request: AuthRequest): Response<ResponseBody>
    @GET("loggedUser")
    suspend fun getUser(@Header("Authorization") token: String): User?
    @GET("{id}/picture")
    suspend fun getUserPicture(@Header("Authorization") token: String, @Path("id") userId: Int): Response<ResponseBody>?
    @PUT("edit")
    suspend fun putUserInfo(@Header("Authorization") token: String, @Body request: AuthRequest): Response<ResponseBody>
    @Multipart
    @PUT("picture")
    suspend fun putUserPicture(@Header("Authorization") token: String, @Part image: MultipartBody.Part): Response<ResponseBody>
    @POST("verifyPassword")
    suspend fun verifyPassword(@Header("Authorization") token: String, @Body password: String): Boolean
    @PATCH("setNewPassword")
    suspend fun setNewPassword(@Header("Authorization") token: String, @Body map: Map<String, String>): Response<ResponseBody>
}
