package com.example.gift4u.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.adaptor.Gift
import com.example.gift4u.adaptor.GiftAdapter

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. 더미 데이터
        val giftData = listOf(
            Gift("바이레도", "애플로에 오 드 퍼퓸", "320,000원~", 0),
            Gift("딥디크", "도손 오 드 퍼퓸", "210,000원~", 0),
            Gift("조말론", "우드 세이지 앤 씨솔트", "190,000원~", 0),
            Gift("샤넬", "넘버5", "250,000원~", 0),
            Gift("크리드", "어벤투스", "450,000원~", 0),
            Gift("르라보", "33", "300,000원~", 0)
        )

        // 2. 첫 번째 리싸이클러뷰 설정 (추천 향수)
        val rvRecommend = view.findViewById<RecyclerView>(R.id.rv_recommend)
        rvRecommend.adapter = GiftAdapter(giftData)
        // SpanCount를 3으로 설정하여 한 줄에 3개씩 표시
        rvRecommend.layoutManager = GridLayoutManager(context, 3)

        // 3. 두 번째 리싸이클러뷰 설정 (요즘 뜨는 향수)
        val rvHighPrice = view.findViewById<RecyclerView>(R.id.rv_high_price)
        rvHighPrice.adapter = GiftAdapter(giftData) // 데이터는 예시로 같은 거 사용
        rvHighPrice.layoutManager = GridLayoutManager(context, 3)

        // 세 번째 리싸이클러뷰 설정
        val rvLowPrice = view.findViewById<RecyclerView>(R.id.rv_low_price)
        rvLowPrice.adapter = GiftAdapter(giftData) // 데이터는 예시로 같은 거 사용
        rvLowPrice.layoutManager = GridLayoutManager(context, 3)

        return view
    }
}