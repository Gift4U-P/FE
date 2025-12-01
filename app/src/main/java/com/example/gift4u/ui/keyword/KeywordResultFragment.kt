package com.example.gift4u.ui.keyword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.adaptor.Gift
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class KeywordResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keyword_result, container, false)

        // 1. 상단 버블 키워드
        val keywords = arguments?.getStringArrayList("keywords") ?: arrayListOf("?", "?", "?", "?")

        // 순서대로: 나이, 성별, 관계, 분위기
        view.findViewById<TextView>(R.id.tv_bubble_age).text = keywords.getOrElse(0) { "" }
        view.findViewById<TextView>(R.id.tv_bubble_gender).text = keywords.getOrElse(1) { "" }
        view.findViewById<TextView>(R.id.tv_bubble_relation).text = keywords.getOrElse(2) { "" }
        view.findViewById<TextView>(R.id.tv_bubble_vibe).text = keywords.getOrElse(3) { "" }

        // 2. 추천 선물 리스트 설정 (GiftTestResultFragment 참고)
        val recommendData = listOf(
            Gift("조말론", "우드 세이지", "190,000원~", 0),
            Gift("딥디크", "도손", "210,000원~", 0),
            Gift("바이레도", "블랑쉬", "320,000원~", 0),
            Gift("샤넬", "넘버5", "250,000원~", 0),
            Gift("크리드", "어벤투스", "450,000원~", 0),
            Gift("르라보", "상탈 33", "300,000원~", 0)
        )

        val rvRecommend = view.findViewById<RecyclerView>(R.id.rv_result_recommend)
        rvRecommend.adapter = GiftAdapter(recommendData)
        // 3열 그리드로 설정 (2줄로 보이게 됨)
        rvRecommend.layoutManager = GridLayoutManager(context, 3)


        // 3. 결과 화면 하단 BNV (저장 / 홈으로) 설정
        val resultBnv = view.findViewById<BottomNavigationView>(R.id.result_bnv)

        resultBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_result_save -> {
                    // 저장 로직 (토스트 메시지 예시) 추후 커스텀 토스트 메세지로 구현 예정
                    Toast.makeText(context, "키워드 결과가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_result_home -> {
                    // 홈으로 이동 로직 (백스택 비우기)
                    (activity as? MainActivity)?.let { mainActivity ->
                        mainActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                        // 메인 바텀 네비게이션의 선택 상태를 '홈'으로 복구
                        val mainBnv = mainActivity.findViewById<BottomNavigationView>(R.id.main_bnv)
                        mainBnv.selectedItemId = R.id.homeFragment
                    }
                    true
                }
                else -> false
            }
        }

        return view
    }

    // 4. 메인 액티비티의 BNV 숨김 처리
    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavVisibility(false)
        }
    }

    override fun onPause() {
        super.onPause()
        // 프래그먼트가 사라질 때(뒤로가기 등) 메인 BNV 다시 보이기
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavVisibility(true)
        }
    }
}