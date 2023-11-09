package com.example.ecommercemobile.data

import android.content.SharedPreferences
import android.util.Log
import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.data.network.ApiAuth
import com.example.ecommercemobile.data.network.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ApiAuth,
    private val prefs: SharedPreferences):AuthService {

    override suspend fun signUp(authRequest: AuthRequest): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.signUp(authRequest)
                logIn(authRequest)
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                if(e.code() == 401) {
                    AuthResult.Unauthorized()
                } else {
                    AuthResult.UnknownError()
                }
            } catch (e: Exception) {
                AuthResult.UnknownError()
            }
        }
    }

    override suspend fun logIn(authRequest: AuthRequest): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.logIn(authRequest)
                prefs.edit()
                    .putString("jwt", response.token)
                    .apply()
                AuthResult.Authorized()
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                if(e.code() == 401) {
                    AuthResult.Unauthorized()
                } else {
                    AuthResult.UnknownError()
                }
            } catch (e: Exception) {
                AuthResult.UnknownError()
            }
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext AuthResult.Unauthorized()
                api.authenticate("Bearer $token")
                AuthResult.Authorized()
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                if(e.code() == 401) {
                    AuthResult.Unauthorized()
                } else {
                    AuthResult.UnknownError()
                }
            } catch (e: Exception) {
                AuthResult.UnknownError()
            }
        }
    }

    override suspend fun getUser(): User? {
        return withContext(Dispatchers.IO) {
            val token = prefs.getString("jwt", null) ?: return@withContext null
            val user = api.getUser("Bearer $token")
            println("Resultado getUser desde repo\n $user")
            user
        }
    }

    override suspend fun getUserPicture(userId: Int): Response<ResponseBody>? {
        return withContext(Dispatchers.IO) {
            val token = prefs.getString("jwt", null) ?: return@withContext null
            api.getUserPicture("Bearer $token", userId)
        }
    }

    override suspend fun putUserInfo(authRequest: AuthRequest): Boolean {
        return withContext(Dispatchers.IO) {
             try {
                 val token = prefs.getString("jwt", null) ?: return@withContext false
                 val response = api.putUserInfo("Bearer $token", authRequest)
                 response.isSuccessful
             } catch(e: HttpException) {
                 Log.e("Error " + e.code(), e.message())
                 false
             } catch (e: Exception) {
                 e.message?.let { Log.e("Error " + e.cause, it) }
                 false
             }
        }
    }

    override suspend fun putUserPicture( image: MultipartBody.Part): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext AuthResult.Unauthorized()
                api.putUserPicture("Bearer $token",  image)
                AuthResult.Authorized()

            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                if(e.code() == 401) {
                    AuthResult.Unauthorized()
                } else {
                    AuthResult.UnknownError()
                }
            } catch (e: Exception) {
                AuthResult.UnknownError()
            }
        }
    }


}