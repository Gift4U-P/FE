package com.example.gift4u.ui.mypage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.adaptor.Big5EvidenceAdapter
import com.example.gift4u.api.home.model.HomeGiftItem
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.adaptor.ResultGiftAdapter
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.mypage.model.SurveyDetailRequest
import com.example.gift4u.api.mypage.model.SurveyDetailResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageDetailFragment : Fragment() {

    private var surveyId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 기존 결과 화면 레이아웃 재사용
        val view = inflater.inflate(R.layout.fragment_gifttestresult, container, false)

        // 전달받은 surveyId 확인
        surveyId = arguments?.getInt("surveyId", -1) ?: -1

        if (surveyId != -1) {
            fetchDetailResult(view, surveyId)
        } else {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        // 하단 메뉴(BNV) 영역 전체 숨기기
        val bottomNavContainer = view.findViewById<View>(R.id.result_bottom_nav_container)
        bottomNavContainer.visibility = View.GONE

        return view
    }



    private fun fetchDetailResult(view: View, id: Int) {
        val request = SurveyDetailRequest(surveyId = id)

        Gift4uClient.myPageService.getSurveyDetail(request).enqueue(object : Callback<SurveyDetailResponse> {
            override fun onResponse(call: Call<SurveyDetailResponse>, response: Response<SurveyDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val result = response.body()!!.result
                    result?.let { updateUI(view, it) }
                } else {
                    Log.e("MyPageDetail", "Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SurveyDetailResponse>, t: Throwable) {
                Log.e("MyPageDetail", "Network Error", t)
            }
        })
    }

    private fun updateUI(view: View, data: com.example.gift4u.api.mypage.model.SurveyDetailResult) {
        view.findViewById<TextView>(R.id.tv_content_personality_analysis).text = data.analysis
        view.findViewById<TextView>(R.id.tv_content_recommend_reasoning).text = data.reasoning
        view.findViewById<TextView>(R.id.tv_content_message_card).text = data.cardMessage

        // Evidence 리스트 연결
        val rvEvidence = view.findViewById<RecyclerView>(R.id.rv_evidence_list)
        rvEvidence.adapter = Big5EvidenceAdapter(data.evidence)
        rvEvidence.layoutManager = LinearLayoutManager(view.context)

        // 1. 전체 리스트 생성 및 정렬
        val fullList = data.giftList.map {
            HomeGiftItem(
                title = it.title,
                lprice = it.lprice,
                link = it.link,
                image = it.image,
                mallName = it.mallName,
                accuracy = it.accuracy
            )
        }.sortedByDescending { it.accuracy }

        // 2. 리스트 분리
        val bestList = fullList.take(3)
        val restList = fullList.drop(3)

        // 3. Best 3 연결 (Rank)
        val rvBest = view.findViewById<RecyclerView>(R.id.rv_gift_best)
        rvBest.adapter = ResultGiftAdapter(bestList, ResultGiftAdapter.TYPE_RANK)
        rvBest.layoutManager = LinearLayoutManager(context)

        // 4. 나머지 연결 (Grid)
        val rvRest = view.findViewById<RecyclerView>(R.id.rv_gift_rest)
        rvRest.adapter = ResultGiftAdapter(restList, ResultGiftAdapter.TYPE_GRID)
        rvRest.layoutManager = GridLayoutManager(context, 3)
    }
}