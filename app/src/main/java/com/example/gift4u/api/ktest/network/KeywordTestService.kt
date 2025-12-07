package com.example.gift4u.api.ktest.network

import com.example.gift4u.api.ktest.model.KeywordResultRequest
import com.example.gift4u.api.ktest.model.KeywordResultResponse
import com.example.gift4u.api.ktest.model.KeywordSaveRequest
import com.example.gift4u.api.ktest.model.KeywordSaveResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface KeywordTestService {

    // 결과 보내고 출력
    @POST("/keyword/result")
    fun sendKeywordResults(@Body request: KeywordResultRequest): Call<KeywordResultResponse>

    // 결과 저장
    @POST("/keyword/save")
    fun saveKeywordResult(@Body request: KeywordSaveRequest): Call<KeywordSaveResponse>
}