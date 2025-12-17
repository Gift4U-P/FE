package com.example.gift4u.api.gtest.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable // Fragment 간 객체 전달

// 1. 요청 바디 (10개 질문)
data class Big5ResultRequest(
    @SerializedName("q1") val q1: String,
    @SerializedName("q2") val q2: String,
    @SerializedName("q3") val q3: String,
    @SerializedName("q4") val q4: String,
    @SerializedName("q5") val q5: String,
    @SerializedName("q6") val q6: String,
    @SerializedName("q7") val q7: String,
    @SerializedName("q8") val q8: String,
    @SerializedName("q9") val q9: String,
    @SerializedName("q10") val q10: String
)

// 2. 선물 아이템 (GiftAdapter의 Gift와 필드가 유사하므로 변환 필요)
data class Big5GiftItem(
    @SerializedName("title") val title: String,
    @SerializedName("lprice") val lprice: String,
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,
    @SerializedName("mallName") val mallName: String,
    @SerializedName("accuracy") val accuracy: Double? = 0.0
) : Serializable

// 근거
data class Big5Evidence(
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String
) : Serializable

// 3. 결과 데이터
data class Big5Result(
    @SerializedName("analysis") val analysis: String,
    @SerializedName("evidence") val evidence: List<Big5Evidence>,
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