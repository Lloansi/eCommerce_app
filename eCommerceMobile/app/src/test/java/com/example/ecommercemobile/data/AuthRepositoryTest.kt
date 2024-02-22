package com.example.ecommercemobile.data

import android.content.SharedPreferences
import com.example.ecommercemobile.data.model.auth.AuthRequest
import com.example.ecommercemobile.data.model.auth.AuthResult
import com.example.ecommercemobile.data.model.auth.TokenResponse
import com.example.ecommercemobile.data.network.auth.ApiAuth
import com.example.ecommercemobile.data.repository.AuthRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AuthRepositoryTest{

    @RelaxedMockK
    private lateinit var authRepository: ApiAuth
    @RelaxedMockK
    private lateinit var prefs: SharedPreferences
    @RelaxedMockK
    lateinit var repository: AuthRepository
    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
    }


    @Test
    fun `signUp should return ServerUnavailable when server is not responding`() = runBlocking {
        //Given
        val authRequest = AuthRequest(" ", " ")
        coEvery { authRepository.signUp(authRequest) } throws Exception()
        //When
        repository.signUp(authRequest)
        //Then
        coVerify { authRepository.signUp(authRequest) }
    }
    @Test
    fun `signUp should return NoConnection when there is no internet connection`() = runBlocking {
        //Given
        val authRequest = AuthRequest(" ", " ")
        coEvery { authRepository.signUp(authRequest) } throws Exception()
        //When
        repository.signUp(authRequest)
        //Then
        coVerify { authRepository.signUp(authRequest) }
    }
    @Test
    fun `logIn should return a TokenResponse when the user tries to logIn`() = runBlocking {
        //Given
        val authRequest = AuthRequest(" ", " ")
        coEvery { authRepository.logIn(authRequest) } returns TokenResponse(" ")
        //When
        repository.logIn(authRequest)
        //Then
        coVerify { authRepository.logIn(authRequest) }
    }
    @Test
    fun `logIn should return a Unauthorized when the AuthRequest is not correct`() = runBlocking {
        //Given
        val authRequest = AuthRequest(" ", " ")
        coEvery { repository.logIn(authRequest) } returns AuthResult.Authorized()
        //When
        repository.logIn(authRequest)
        //Then
        coVerify { repository.logIn(authRequest) }
    }

    fun `signUp should return UnknownError when there is an unknown error`() = runBlocking {
        //Given
        val authRequest = AuthRequest(" ", " ")
        coEvery { authRepository.signUp(authRequest) } throws Exception()
        //When
        repository.signUp(authRequest)
        //Then
        coVerify { authRepository.signUp(authRequest) }
    }

}