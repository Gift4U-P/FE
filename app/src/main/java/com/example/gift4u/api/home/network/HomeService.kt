package com.example.gift4u.api.home.network

import com.example.gift4u.api.home.model.HomeGiftListResponse
import retrofit2.Call
import retrofit2.http.GET

interface HomeService {
    // 홈 화면 선물 리스트 조회
    @GET("/users/present/list")
    fun getHomeGiftLists(): Call<HomeGiftListResponse>
}