package com.example.ecommercemobile.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.ecommercemobile.data.repository.OrdersRepository
import com.example.ecommercemobile.data.repository.AuthRepository
import com.example.ecommercemobile.data.repository.ProductsRepository
import com.example.ecommercemobile.data.network.auth.ApiAuth
import com.example.ecommercemobile.data.network.products.ApiProducts
import com.example.ecommercemobile.data.network.orders.ApiOrders
import com.example.ecommercemobile.data.network.auth.AuthService
import com.example.ecommercemobile.data.network.products.ProductsService
import com.example.ecommercemobile.data.network.orders.OrdersService
import com.example.ecommercemobile.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideProductsApi(): ApiProducts {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.PRODUCT_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideAuthApi(): ApiAuth {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val client = OkHttpClient.Builder()
            //.addInterceptor(AuthInterceptor(token))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.AUTH_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideRelationApi(): ApiOrders {
        val gson = GsonBuilder()
            .serializeNulls()
            .create()
        val client = OkHttpClient.Builder()
            //.addInterceptor(AuthInterceptor(token))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.REL_API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create()
    }
    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }
    @Provides
    @Singleton
    fun provideAuthRepository(api: ApiAuth, prefs: SharedPreferences): AuthService {
        return AuthRepository(api, prefs)
    }
    @Provides
    @Singleton
    fun provideRelationRepository(relationApi: ApiOrders, prefs: SharedPreferences): OrdersService {
        return OrdersRepository(relationApi, prefs)
    }
    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ApiProducts): ProductsService {
        return ProductsRepository(productsApi)
    }
}