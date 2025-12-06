package com.example.gift4u.api.gtest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable // Fragment 간 객체 전달

// 1. 요청 바디 (10개 질문)
data class Big5ResultRequest(
    @SerializedName("qone") val qone: String,
    @SerializedName("qtwo") val qtwo: String,
    @SerializedName("qthree") val qthree: String,
    @SerializedName("qfour") val qfour: String,
    @SerializedName("qfive") val qfive: String,
    @SerializedName("qsix") val qsix: String,
    @SerializedName("qseven") val qseven: String,
    @SerializedName("qeight") val qeight: String,
    @SerializedName("qnine") val qnine: String,
    @SerializedName("qten") val qten: String
)

// 2. 선물 아이템 (GiftAdapter의 Gift와 필드가 유사하므로 변환 필요)
data class Big5GiftItem(
    @SerializedName("title") val title: String,
    @SerializedName("lprice") val lprice: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("mallName") val mallName: String
) : Serializable

// 3. 결과 데이터
data class Big5Result(
    @SerializedName("analysis") val analysis: String,
    @SerializedName("reasoning") val reasoning: String,
    @SerializedName("card_message") val cardMessage: String,
    @SerializedName("giftList") val giftList: List<Big5GiftItem>
) : Serializable

// 4. 최종 응답
data class Big5ResultResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Big5Result?
)

// 결과 저장 요청 Body
data class SurveySaveRequest(
    @SerializedName("savedName") val savedName: String
)

// 결과 저장 응답
data class SurveySaveResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("savedName") val savedName: String,
    @SerializedName("createdAt") val createdAt: String
)