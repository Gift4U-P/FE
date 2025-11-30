package com.example.gift4u.ui.gtest

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.adaptor.Gift
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class GiftTestResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttestresult, container, false)

        // --- 1. 게이지 바 설정 (더미 데이터 예시) ---
        setupGauge(view.findViewById(R.id.gauge_1), "개방성", "보수성", 75, "#D5F5E3") // 초록
        setupGauge(view.findViewById(R.id.gauge_2), "성실성", "유연성", 67, "#FADBD8") // 분홍
        setupGauge(view.findViewById(R.id.gauge_3), "외향성", "내향성", 58, "#D6EAF8") // 파랑
        setupGauge(view.findViewById(R.id.gauge_4), "우호성", "독자성", 52, "#FAE5D3") // 노랑
        setupGauge(view.findViewById(R.id.gauge_5), "안정성", "신경성", 90, "#E8DAEF") // 보라

        // --- 2. 카테고리 추천 (1행 3개) ---
        // item_home_item.xml을 재사용하므로 Gift 객체로 더미 데이터 생성
        val categoryData = listOf(
            Gift("카테고리", "향수 세트", "20대 추천", 0),
            Gift("카테고리", "바디 미스트", "30대 추천", 0),
            Gift("카테고리", "디퓨저", "선물용", 0)
        )
        val rvCategory = view.findViewById<RecyclerView>(R.id.rv_result_category)
        rvCategory.adapter = GiftAdapter(categoryData)
        rvCategory.layoutManager = GridLayoutManager(context, 3) // 3열 그리드

        // --- 3. 선물 추천 (2행 3개 = 총 6개) ---
        val recommendData = listOf(
            Gift("조말론", "우드 세이지", "190,000원~", 0),
            Gift("딥디크", "도손", "210,000원~", 0),
            Gift("바이레도", "블랑쉬", "320,000원~", 0),
            Gift("샤넬", "넘버5", "250,000원~", 0),
            Gift("크리드", "어벤투스", "450,000원~", 0),
            Gift("르라보", "33", "300,000원~", 0)
        )
        val rvRecommend = view.findViewById<RecyclerView>(R.id.rv_result_recommend)
        rvRecommend.adapter = GiftAdapter(recommendData)
        rvRecommend.layoutManager = GridLayoutManager(context, 3) // 3열 그리드 -> 데이터가 6개이므로 2행이 됨

        // --- [수정됨] 결과 화면 전용 BNV 설정 ---
        val resultBnv = view.findViewById<BottomNavigationView>(R.id.result_bnv)

        resultBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_result_save -> {
                    // TODO: 저장 로직 구현
                    // 예: Toast.makeText(context, "결과가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.menu_result_home -> {
                    // 홈으로 이동 로직
                    (activity as? MainActivity)?.let { mainActivity ->
                        // 백스택 모두 비우기
                        mainActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                        // 메인 BNV의 선택 상태를 '홈'으로 변경
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

    // 게이지 뷰 설정 헬퍼 함수
    private fun setupGauge(view: View, leftText: String, rightText: String, percent: Int, colorHex: String) {
        val tvLeft = view.findViewById<TextView>(R.id.tv_left_label)
        val tvRight = view.findViewById<TextView>(R.id.tv_right_label)
        val tvPercent = view.findViewById<TextView>(R.id.tv_percent)
        val viewFill = view.findViewById<View>(R.id.view_gauge_fill)

        tvLeft.text = leftText
        tvRight.text = rightText
        tvPercent.text = "${percent}%"

        // 게이지 바 색상 설정
        viewFill.backgroundTintList = ColorStateList.valueOf(Color.parseColor(colorHex))

        // 게이지 채워짐 정도 (LayoutParams weight 변경 방식이 가장 확실함, 여기서는 너비를 동적으로 계산하거나 간단히 구현)
        // CardView 내부라 width를 동적으로 변경합니다.
        viewFill.post {
            val parentWidth = (viewFill.parent as View).width
            val params = viewFill.layoutParams
            params.width = (parentWidth * (percent / 100.0)).toInt()
            viewFill.layoutParams = params
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavVisibility(false)
        }
    }

    override fun onPause() {
        super.onPause()
        if (activity is MainActivity) {
            (activity as MainActivity).setBottomNavVisibility(true)
        }
    }
}