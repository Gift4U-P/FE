package com.example.gift4u.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.home.model.HomeGiftListResponse
import com.example.gift4u.api.mypage.model.UserProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var tvGreeting: TextView
    private lateinit var rvRecommend: RecyclerView
    private lateinit var rvHighPrice: RecyclerView
    private lateinit var rvLowPrice: RecyclerView

    // 어댑터 인스턴스 (나중에 데이터를 채워넣기 위해 미리 선언)
    private lateinit var adapterRecommend: GiftAdapter
    private lateinit var adapterHighPrice: GiftAdapter
    private lateinit var adapterLowPrice: GiftAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // 1. 뷰 초기화
        tvGreeting = view.findViewById(R.id.tv_greeting)
        rvRecommend = view.findViewById(R.id.rv_recommend)
        rvHighPrice = view.findViewById(R.id.rv_high_price)
        rvLowPrice = view.findViewById(R.id.rv_low_price)

        // 리사이클러뷰 초기설정(빈 어댑터 연결)
        setupRecyclerViews()

        // API 호출
        fetchUserProfile() // 닉네임 로드
        fetchGiftLists()   // 선물 리스트 로드

        return view
    }

    private fun setupRecyclerViews() {
        // 어댑터 초기화 (빈 리스트로 시작)
        adapterRecommend = GiftAdapter(emptyList())
        adapterHighPrice = GiftAdapter(emptyList())
        adapterLowPrice = GiftAdapter(emptyList())

        // LayoutManager 및 Adapter 연결
        rvRecommend.layoutManager = GridLayoutManager(context, 3)
        rvRecommend.adapter = adapterRecommend

        rvHighPrice.layoutManager = GridLayoutManager(context, 3)
        rvHighPrice.adapter = adapterHighPrice

        rvLowPrice.layoutManager = GridLayoutManager(context, 3)
        rvLowPrice.adapter = adapterLowPrice
    }

    // 선물 리스트 API 호출
    private fun fetchGiftLists() {
        Gift4uClient.homeService.getHomeGiftLists().enqueue(object : Callback<HomeGiftListResponse> {
            override fun onResponse(
                call: Call<HomeGiftListResponse>,
                response: Response<HomeGiftListResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        val result = resp.result

                        result?.let {
                            // 각 리스트 데이터 업데이트
                            // 1. 이런 선물 어떠세요? -> randomGifts
                            adapterRecommend.updateList(it.randomGifts)

                            // 2. 럭셔리 브랜드 -> luxuryGifts
                            adapterHighPrice.updateList(it.luxuryGifts)

                            // 3. 5만원대 이하 가성비 -> budgetGifts
                            adapterLowPrice.updateList(it.budgetGifts)
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Gift List Load Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<HomeGiftListResponse>, t: Throwable) {
                Log.e("HomeFragment", "Network Error", t)
                // Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 사용자 프로필 API 호출
    private fun fetchUserProfile() {
        Gift4uClient.myPageService.getUserProfile().enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        val result = resp.result

                        // 데이터 바인딩
                        result?.let {
                            // 닉네임을 포함한 인사말 설정
                            tvGreeting.text = "반가워요! ${it.name}님\nGift4U 에서 의미있는 선물을 찾아보세요."
                        }
                    }
                } else {
                    Log.e("HomeFragment", "Nickname Load Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("HomeFragment", "Profile Network Error", t)
            }
        })
    }
}