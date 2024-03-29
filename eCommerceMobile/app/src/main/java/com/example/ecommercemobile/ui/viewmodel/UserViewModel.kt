package com.example.ecommercemobile.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.data.repository.AuthRepository
import com.example.ecommercemobile.data.model.User
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val user = MutableLiveData<User?>()
    val authResult = MutableLiveData<AuthResult<Unit>>()
    val userImage = MutableLiveData<Bitmap>()
    val isPasswordChanged = MutableLiveData<Boolean>()

    init {
        println("VIEWMODEL-init()-authenticate()")
        authenticate()
    }

    fun authenticate() {
        println("VIEWMODEL-authenticate()")
        viewModelScope.launch {
            val result = authRepository.authenticate()
            authResult.postValue(result)
        }
    }

    fun logIn(authRequest: AuthRequest) {
        println("VIEWMODEL-logIn()")
        viewModelScope.launch {
            val result = authRepository.logIn(authRequest)
            authResult.postValue(result)
        }
    }

    fun signUp(authRequest: AuthRequest) {
        println("VIEWMODEL-signUp()")
        viewModelScope.launch {
            val result = authRepository.signUp(authRequest)
            authResult.postValue(result)
        }
    }

    fun getUser() {
        println("VIEWMODEL-getUser()")
        viewModelScope.launch {
            val result = authRepository.getUser()
            if (result != null) {
                user.postValue(result)
            }
        }
    }

    fun getUserPicture(userID: Int) {
        viewModelScope.launch {
            val response = authRepository.getUserPicture(userID)
            if (response!!.isSuccessful) {
                val source = response.body()
                val inputStream = source?.byteStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                userImage.postValue(bitmap)
            } else {
                Log.e("Error " + response.code(), response.message())
            }
        }

    }

    fun putUserPicture(imageFile: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = authRepository.putUserPicture(MultipartBody.Part.createFormData("image", imageFile.name, imageFile.asRequestBody("image/*".toMediaTypeOrNull())))
            if (result is AuthResult.Authorized) {
                getUserPicture(user.value!!.userID)
            }
        }
    }

    fun setNewPassword(map: Map<String, String>){
        viewModelScope.launch {
            val result = authRepository.setNewPassword(map)
            if (result) {
                isPasswordChanged.postValue(true)
            }
        }
    }

    fun resetPassword(email:String) {
        viewModelScope.launch {
            authRepository.resetPasswordEmail(email)
        }
    }

    fun changeEmail(oldMail:String, newMail:String, password: String) {
        viewModelScope.launch {
            authRepository.changeEmail(oldMail, newMail, password)
            val result = authRepository.authenticate()
            authResult.postValue(result)
        }
    }

    fun validateEmail(email:String) {
        viewModelScope.launch {
            authRepository.validateEmail(email)
        }
    }

    fun putUser(email: String, pass: String) {
        viewModelScope.launch {
            if (authRepository.putUserInfo(AuthRequest(email,pass))) {
                val result = authRepository.logIn(AuthRequest(email, pass))
                if (result is AuthResult.Authorized) {
                    getUser()
                }
            }
        }
    }

    fun checkEmailSyntax(emailET: TextInputLayout, email: String): Boolean {
        val emailPattern = Regex(
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)\$"
        )

        return if (!emailPattern.matches(email)) {
            emailET.isErrorEnabled = true
            emailET.error = "Invalid email"
            false
        } else {
            emailET.isErrorEnabled = false
            emailET.error = null
            true
        }
    }

    fun checkPasswordSyntax(passET: TextInputLayout, password: String): Boolean {
        passET.error = null
        val minus = Regex("[a-z]")
        val mayus = Regex("[A-Z]")
        val number = Regex("[0-9]")
        return if (password.length <= 7 || !minus.containsMatchIn(password)
            || !mayus.containsMatchIn(password) || !number.containsMatchIn(password)) {
            passET.isErrorEnabled = true
            passET.error = "Password must be at least 8 characters long and contain at least one uppercase letter and one digit."
            false
        }else{
            passET.error = null
            passET.isErrorEnabled = false
            true
        }
    }

    fun confirmPassword(confirmPassET: TextInputLayout, confPassword: String, password: String): Boolean {
        return if (confPassword.trim() != password) {
            confirmPassET.isErrorEnabled = true
            confirmPassET.error = "The passwords don't match."
            false
        } else {
            confirmPassET.error = null
            confirmPassET.isErrorEnabled = false // Quita el espacio extra
            true
        }
    }

    fun encryptPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hash = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        val hex = StringBuilder(hash.size * 2)

        for (byte in hash) {
            val hexString = Integer.toHexString(0xff and byte.toInt())
            if (hexString.length == 1) {
                hex.append('0')
            }
            hex.append(hexString)
        }

        return hex.toString()
    }
}
