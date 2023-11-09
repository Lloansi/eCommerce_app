package com.example.ecommercemobile.data.network

import com.example.ecommercemobile.data.model.PopularProduct
import com.example.ecommercemobile.data.model.RecommendedProduct
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MongoApiClient {

    //POPULAR
    @GET("/allPopularProducts")
    suspend fun getAllPopularProducts(): Response<List<PopularProduct>>
    @GET("/all/{id}")
    suspend fun getPopularProductById(@Path("id") url: String): Response<PopularProduct>
    @POST("/allPopularProducts")
    @Multipart
    suspend fun postPopularProduct(@Part("allPopularProducts") popularProduct: RequestBody, @Part image: MultipartBody.Part): Response<ResponseBody>
    @PUT("/allPopularProducts/{id}")
    @Multipart
    suspend fun putPopularProduct(@Path("id") url: String, @Part("allPopularProducts") popularProduct: RequestBody, @Part image: MultipartBody.Part): Response<ResponseBody>
    @DELETE("/allPopularProducts/{id}")
    suspend fun deletePopularProduct(@Path("id") url: String): Response<ResponseBody>

    //POPULAR
    @GET("/allRecommendedProducts")
    suspend fun getAllRecommendedProducts(): Response<List<RecommendedProduct>>
    @GET("/all/{id}")
    suspend fun getRecommendedProductById(@Path("id") url: String): Response<RecommendedProduct>
    @POST("/allRecommendedProducts")
    @Multipart
    suspend fun postRecommendedProduct(@Part("allRecommendedProducts") recommendedProduct: RequestBody, @Part image: MultipartBody.Part): Response<ResponseBody>
    @PUT("/allRecommendedProducts/{id}")
    @Multipart
    suspend fun putRecommendedProduct(@Path("id") url: String, @Part("allRecommendedProducts") recommendedProduct: RequestBody, @Part image: MultipartBody.Part): Response<ResponseBody>
    @DELETE("/allRecommendedProducts/{id}")
    suspend fun deleteRecommendedProduct(@Path("id") url: String): Response<ResponseBody>

}