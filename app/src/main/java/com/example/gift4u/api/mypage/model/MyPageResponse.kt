package com.example.gift4u.api.mypage.model

import com.google.gson.annotations.SerializedName

// 1. 최상위 응답 (서버가 주는 전체 JSON)
data class SurveyResultListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    // result가 리스트가 아니라, 'result'라는 필드를 가진 객체(Wrapper)입니다.
    @SerializedName("result") val result: SurveyResultWrapper?
)

// 2. 중간 래퍼 클래스 (result 객체 내부)
data class SurveyResultWrapper(
    @SerializedName("result") val surveyList: List<SurveyResultItem>?
)

// 3. 실제 데이터 아이템
data class SurveyResultItem(
    @SerializedName("surveyId") val surveyId: Int,
    @SerializedName("savedName") val savedName: String,
    @SerializedName("createdAt") val createdAt: String
)

// 프로필 조회 응답
data class UserProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: UserProfileResult?
)

// 프로필 실제 데이터
data class UserProfileResult(
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String, // "MALE" or "FEMALE"
    @SerializedName("email") val email: String
)