package com.example.gift4u.api.mypage.model

import com.example.gift4u.api.gtest.model.Big5GiftItem
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
    // surveyId 또는 keywordId 중 어느 것이 와도 이 변수에 저장
    @SerializedName("surveyId", alternate = ["keywordId"])
    val surveyId: Int,
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

// 1. big5 상세 조회 요청 Body
data class SurveyDetailRequest(
    @SerializedName("surveyId") val surveyId: Int
)

// 2. big5 상세 조회 응답 Wrapper
data class SurveyDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SurveyDetailResult?
)

// 3. big5 상세 조회 실제 데이터
data class SurveyDetailResult(
    @SerializedName("savedName") val savedName: String,
    @SerializedName("analysis") val analysis: String,
    @SerializedName("reasoning") val reasoning: String,
    @SerializedName("card_message") val cardMessage: String,
    @SerializedName("giftList") val giftList: List<Big5GiftItem>
)

// 1. 키워드 상세 조회 요청 Body
data class KeywordDetailRequest(
    @SerializedName("keywordId") val keywordId: Int
)

// 2. 키워드 상세 조회 응답 Wrapper
data class KeywordDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: KeywordDetailResult?
)

// 3. 키워드 상세 조회 실제 데이터
data class KeywordDetailResult(
    @SerializedName("savedName") val savedName: String,
    @SerializedName("age") val age: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("situation") val situation: String,
    @SerializedName("keywordText") val keywordText: String,
    @SerializedName("card_message") val cardMessage: String,
    @SerializedName("giftList") val giftList: List<Big5GiftItem>
)