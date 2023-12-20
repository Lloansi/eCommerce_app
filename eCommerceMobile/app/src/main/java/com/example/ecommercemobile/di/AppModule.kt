package com.example.ecommercemobile.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.ecommercemobile.data.RelationRepository
import com.example.ecommercemobile.data.Repository
import com.example.ecommercemobile.data.RepositoryProducts
import com.example.ecommercemobile.data.network.ApiAuth
import com.example.ecommercemobile.data.network.ApiProducts
import com.example.ecommercemobile.data.network.ApiRelation
import com.example.ecommercemobile.data.network.AuthService
import com.example.ecommercemobile.data.network.ProductsService
import com.example.ecommercemobile.data.network.RelationService
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
    fun provideRelationApi(): ApiRelation {
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
        return Repository(api, prefs)
    }
    @Provides
    @Singleton
    fun provideRelationRepository(relationApi: ApiRelation, prefs: SharedPreferences): RelationService {
        return RelationRepository(relationApi, prefs)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ApiProducts ): ProductsService {
        return RepositoryProducts(productsApi)
    }

}