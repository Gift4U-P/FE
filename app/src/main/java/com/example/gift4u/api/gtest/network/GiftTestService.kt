package com.example.gift4u.api.gtest.network

import com.example.gift4u.api.gtest.model.Big5ResultRequest
import com.example.gift4u.api.gtest.model.Big5ResultResponse
import com.example.gift4u.api.gtest.model.SurveySaveRequest
import com.example.gift4u.api.gtest.model.SurveySaveResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GiftTestService {

    // 결과 보내고 출력
    @POST("/survey/result")
    fun sendBig5Results(@Body request: Big5ResultRequest): Call<Big5ResultResponse>

    // 결과 저장
    @POST("/survey/save")
    fun saveSurveyResult(@Body request: SurveySaveRequest): Call<SurveySaveResponse>
}