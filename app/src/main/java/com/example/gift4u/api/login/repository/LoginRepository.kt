package com.example.gift4u.api.login.repository

import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.login.model.*
import retrofit2.Call

class LoginRepository {

    // Gift4uClient에서 loginService 가져오기
    private val service = Gift4uClient.loginService

    // 회원가입 요청
    fun signUp(request: SignupRequest): Call<SignupResponse> {
        return service.signUp(request)
    }

    // 로그인 요청
    fun login(request: LoginRequest): Call<LoginResponse> {
        return service.login(request)
    }

    // 토큰 재발급 요청
    fun refreshToken(request: RefreshRequest): Call<RefreshResponse> {
        return service.refreshToken(request)
    }
}