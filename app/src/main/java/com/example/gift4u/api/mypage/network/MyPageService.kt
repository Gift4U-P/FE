package com.example.gift4u.api.mypage.network

import com.example.gift4u.api.mypage.model.SurveyDetailRequest
import com.example.gift4u.api.mypage.model.SurveyDetailResponse
import com.example.gift4u.api.mypage.model.SurveyResultListResponse
import com.example.gift4u.api.mypage.model.UserProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyPageService {
    // Header의 Authorization은 Client의 인터셉터가 자동으로 넣어줌
    //  Big 5 설문 결과 리스트 조회
    @GET("/survey/result/list")
    fun getSurveyResults(): Call<SurveyResultListResponse>

    // 키워드 설문 결과 조회
    @GET("/keyword/result/list")
    fun getKeywordResults(): Call<SurveyResultListResponse>

    // 회원 프로필 조회
    @GET("/users/profile")
    fun getUserProfile(): Call<UserProfileResponse>

    // [추가] 설문 상세 결과 조회
    @POST("/survey/detailResult")
    fun getSurveyDetail(@Body request: SurveyDetailRequest): Call<SurveyDetailResponse>
}