package com.example.gift4u.api.login.model

import com.google.gson.annotations.SerializedName

// 회원가입
data class SignupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class SignupResponse(
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("createdAt") val createdAt: String
)

// 로그인
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginResult? // 실패 시 null일 수 있음
)

// 로그인 응답 내부 데이터
data class LoginResult(
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String
)

// 토큰 재발급
data class RefreshRequest(
    @SerializedName("refreshToken") val refreshToken: String
)

// 재발급 응답 (BaseResponse 형태)
data class RefreshResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: RefreshResult?
)

data class RefreshResult(
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("name") val name: String
)