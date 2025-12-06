package com.example.gift4u.api.home.model

import com.google.gson.annotations.SerializedName

// 1. 전체 응답 Wrapper
data class HomeGiftListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: HomeGiftResult?
)

// 2. 결과 객체 (세 가지 리스트 포함)
data class HomeGiftResult(
    @SerializedName("randomGifts") val randomGifts: List<HomeGiftItem>,
    @SerializedName("luxuryGifts") val luxuryGifts: List<HomeGiftItem>,
    @SerializedName("budgetGifts") val budgetGifts: List<HomeGiftItem>
)

// 3. 개별 선물 아이템
data class HomeGiftItem(
    @SerializedName("title") val title: String,
    @SerializedName("lprice") val lprice: String, // 가격 (String 형태)
    @SerializedName("link") val link: String,
    @SerializedName("image") val image: String,   // 이미지 URL
    @SerializedName("mallName") val mallName: String // 브랜드/몰 이름
)