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
import com.example.gift4u.api.home.model.HomeGiftItem
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

        // 2. 추천 선물 리스트 설정 (더미 데이터 사용)
        // giftList 변수가 없으므로, 직접 HomeGiftItem 리스트를 만듦
        // API 연결 후 GiftTestResult처럼 API 응답(response.body().result.giftList)으로 교체
        val recommendData = listOf(
            HomeGiftItem(
                title = "조말론 우드 세이지 앤 씨솔트",
                lprice = "190000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_8463788/84637882679.23.jpg", // 예시 이미지 URL
                mallName = "조말론"
            ),
            HomeGiftItem(
                title = "딥디크 도손 오 드 퍼퓸",
                lprice = "210000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_8268198/82681982517.7.jpg",
                mallName = "딥디크"
            ),
            HomeGiftItem(
                title = "바이레도 블랑쉬",
                lprice = "320000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_8865141/88651411414.3.jpg",
                mallName = "바이레도"
            ),
            HomeGiftItem(
                title = "샤넬 넘버5",
                lprice = "250000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_5757750/57577503467.jpg",
                mallName = "샤넬"
            ),
            HomeGiftItem(
                title = "크리드 어벤투스",
                lprice = "450000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_4793739/47937397265.20240523214534.jpg",
                mallName = "크리드"
            ),
            HomeGiftItem(
                title = "르라보 상탈 33",
                lprice = "300000",
                link = "",
                image = "https://shopping-phinf.pstatic.net/main_8609149/86091497049.4.jpg",
                mallName = "르라보"
            )
        )

        // 리사이클러뷰 연결
        val rvRecommend = view.findViewById<RecyclerView>(R.id.rv_result_recommend)
        rvRecommend.adapter = GiftAdapter(recommendData) // HomeGiftItem 리스트 구조만 재사용(내부데이터는 api로 받아올 예정)
        rvRecommend.layoutManager = GridLayoutManager(context, 3) // 3열 그리드


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