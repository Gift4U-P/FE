package com.example.gift4u.api.ktest.model

import com.example.gift4u.api.gtest.model.Big5Result
import com.google.gson.annotations.SerializedName

// 1. 요청 바디
data class KeywordResultRequest(
    @SerializedName("age") val age: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("situation") val situation: String
    )

// 최종 응답
data class KeywordResultResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Big5Result?
)

// 결과 저장 요청 Body
data class KeywordSaveRequest(
    @SerializedName("savedName") val savedName: String
)

// 결과 저장 응답
data class KeywordSaveResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("savedName") val savedName: String,
    @SerializedName("createdAt") val createdAt: String
)
