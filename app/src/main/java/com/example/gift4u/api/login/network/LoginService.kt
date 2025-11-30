package com.example.gift4u.api.login.network

import com.example.gift4u.api.login.model.*

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface LoginService {

    @POST("/users/signup")
    fun signUp(
        @Body request: SignupRequest
    ): Call<SignupResponse>

    @POST("/users/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("/users/refresh")
    fun refreshToken(
        @Body request: RefreshRequest
    ): Call<RefreshResponse>
}