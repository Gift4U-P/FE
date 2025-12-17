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
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.adaptor.ResultGiftAdapter
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.home.model.HomeGiftItem
import com.example.gift4u.api.mypage.model.KeywordDetailRequest
import com.example.gift4u.api.mypage.model.KeywordDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageKeywordDetailFragment : Fragment() {

    private var keywordId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 키워드 결과 레이아웃 재사용
        val view = inflater.inflate(R.layout.fragment_keyword_result, container, false)

        // 전달받은 ID 확인
        keywordId = arguments?.getInt("surveyId", -1) ?: -1

        if (keywordId != -1) {
            fetchDetailResult(view, keywordId)
        } else {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        // 하단 메뉴(BNV) 영역 숨기기 (저장된 결과이므로)
        view.findViewById<View>(R.id.bottom_area).visibility = View.GONE

        return view
    }

    private fun fetchDetailResult(view: View, id: Int) {
        val request = KeywordDetailRequest(keywordId = id)

        Gift4uClient.myPageService.getKeywordDetail(request).enqueue(object : Callback<KeywordDetailResponse> {
            override fun onResponse(call: Call<KeywordDetailResponse>, response: Response<KeywordDetailResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val result = response.body()!!.result
                    result?.let { updateUI(view, it) }
                } else {
                    Log.e("KeywordDetail", "Failed: ${response.code()}")
                    Toast.makeText(context, "상세 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<KeywordDetailResponse>, t: Throwable) {
                Log.e("KeywordDetail", "Network Error", t)
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(view: View, data: com.example.gift4u.api.mypage.model.KeywordDetailResult) {
        // 상단 버블 텍스트 설정
        view.findViewById<TextView>(R.id.tv_bubble_age).text = data.age
        view.findViewById<TextView>(R.id.tv_bubble_gender).text = data.gender
        view.findViewById<TextView>(R.id.tv_bubble_relation).text = data.relationship
        view.findViewById<TextView>(R.id.tv_bubble_situation).text = data.situation

        // 카드 메시지 설정
        view.findViewById<TextView>(R.id.tv_content_message_card).text = data.cardMessage

        // 추천 멘트 설정
        val recommendLabel = view.findViewById<TextView>(R.id.tv_keywordText_recommend_label)
        recommendLabel.text = "${data.age} ${data.gender} ${data.relationship}의 ${data.situation}에는\n이런 선물을 추천해요!"

        // 전체 리스트 생성 및 정렬
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

        // 리스트 분리
        val bestList = fullList.take(3)
        val restList = fullList.drop(3)

        // Best 3 연결 (Rank)
        val rvBest = view.findViewById<RecyclerView>(R.id.rv_gift_best)
        rvBest.adapter = ResultGiftAdapter(bestList, ResultGiftAdapter.TYPE_RANK)
        rvBest.layoutManager = LinearLayoutManager(context)

        // 나머지 연결 (Grid)
        val rvRest = view.findViewById<RecyclerView>(R.id.rv_gift_rest)
        rvRest.adapter = ResultGiftAdapter(restList, ResultGiftAdapter.TYPE_GRID)
        rvRest.layoutManager = GridLayoutManager(context, 3)
    }
}