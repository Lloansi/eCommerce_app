package com.example.ecommercemobile.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.data.network.auth.ApiAuth
import com.example.ecommercemobile.data.network.auth.AuthService
import com.example.ecommercemobile.utils.Constants
import com.resend.Resend
import com.resend.services.emails.model.SendEmailRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiAuth,
    private val prefs: SharedPreferences): AuthService {

    override suspend fun authenticate(): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val token =
                    prefs.getString("jwt", null) ?: return@withContext AuthResult.Unauthorized()
                api.authenticate("Bearer $token")
                AuthResult.Authorized()
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
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
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
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

    override suspend fun signUp(authRequest: AuthRequest): AuthResult<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                api.signUp(authRequest)
                logIn(authRequest)
            } catch (e: SocketTimeoutException) {
                // Handle timeout exception (Server took too long to respond)
                Log.e("Connection timeout: Server is not responding",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: UnknownHostException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
            } catch (e: ConnectException) {
                // Handle connection exception (Server unreachable)
                Log.e("Server unreachable",e.message.toString())
                AuthResult.ServerUnavailable()
            } catch (e: IOException) {
                Log.e("NoConnectivityException", e.message.toString())
                AuthResult.NoConnection()
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

    override suspend fun verifyPassword(password: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null)
                api.verifyPassword("Bearer $token",  password)
                true
            } catch(e: HttpException) {
                Log.e("Error " + e.code(), e.message())
                if(e.code() == 401) {
                    false
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun setNewPassword(map: Map<String, String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val token = prefs.getString("jwt", null) ?: return@withContext false
                val response = api.setNewPassword("Bearer $token", map)
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

    override suspend fun resetPasswordEmail(email: String): Boolean {
        return withContext(Dispatchers.IO){
            try{
                val resend = Resend("re_WXkr4E7i_M4kStT7DnVB15Pi4N3ks1CaC")
                val url = "${Constants.AUTH_API_URL}resetPasswordPage/$email"
                val sendEmailRequest = SendEmailRequest.builder()
                    .from("noreply@vhost.es")
                    .to(email)
                    .subject("Reset Password")
                    .html("<p>To reset your password click on the button below:</p>" +
                            "<form action=\"$url\"><button type=\"submit\" href=\"$url\" style=\"display: inline-block; padding: 10px 20px; background-color: #FF8400; color: #fff; text-decoration: none; border-radius: 5px;\">Reset Password</button></form>" +
                            "<p>If the confirmation button does not work, you can also copy and paste the following link into your browser:</p>" +
                            "<p>$url</p>" +
                            "<p>If you did not request a password change, please ignore this email.</p>" +
                            "<p>Thank you,<br>The eCommerceAPP Team</p>")
                    .build()
                resend.emails().send(sendEmailRequest)
                true
            } catch (e: HttpException){
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun changeEmail(oldMail:String, newMail:String, password: String):Boolean {
        return withContext(Dispatchers.IO){
            try {
                val token = prefs.getString("jwt", null)
                val isPassVerified = api.verifyPassword("Bearer $token",  password)
                if (isPassVerified) {
                    prefs.edit().putString("jwt", null).apply()
                    val resend = Resend("re_WXkr4E7i_M4kStT7DnVB15Pi4N3ks1CaC")
                    val url = "${Constants.AUTH_API_URL}changeEmail/${oldMail}/${newMail}"
                    val sendEmailRequest = SendEmailRequest.builder()
                        .from("noreply@vhost.es")
                        .to(newMail)
                        .subject("Set new mail ")
                        .html("<p>To set this as your new mail click below:</p>" +
                                "<form action=\"$url\"><button type=\"submit\" href=\"$url\" style=\"display: inline-block; padding: 10px 20px; background-color: #FF8400; color: #fff; text-decoration: none; border-radius: 5px;\">Reset Email</button></form>" +
                                "<p>If the confirmation button does not work, you can also copy and paste the following link into your browser:</p>" +
                                "<p>$url</p>" +
                                "<p>If you did not request a password change, please ignore this email.</p>" +
                                "<p>Thank you,<br>The eCommerceAPP Team</p>")
                        .build()
                    resend.emails().send(sendEmailRequest)
                }
                isPassVerified
            } catch (e: HttpException){
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                false
            }
        }
    }

    override suspend fun validateEmail(email: String): Boolean {
        return withContext(Dispatchers.IO){
            try{
                val resend = Resend("re_WXkr4E7i_M4kStT7DnVB15Pi4N3ks1CaC")
                val url = "${Constants.AUTH_API_URL}validatePage/$email"
                val sendEmailRequest = SendEmailRequest.builder()
                    .from("noreply@vhost.es")
                    .to(email)
                    .subject("Email Validation")
                    .html("<p>Thank you for registering with our application! To complete the registration process, please click on the confirmation link below:</p>" +
                            "<form action=\"$url\"><button type=\"submit\" href=\"$url\" style=\"display: inline-block; padding: 10px 20px; background-color: #FF8400; color: #fff; text-decoration: none; border-radius: 5px;\">Confirm Registration</button></form>" +
                            "<p>If the confirmation button does not work, you can also copy and paste the following link into your browser:</p>" +
                            "<p>$url</p>" +
                            "<p>Thank you,<br>The eCommerceAPP Team</p>"
                    )
                    .build()
                resend.emails().send(sendEmailRequest)
                true
            } catch (e: HttpException){
                Log.e("Error " + e.code(), e.message())
                false
            } catch (e: Exception) {
                false
            }
        }
    }
}