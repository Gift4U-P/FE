package com.example.gift4u.ui.gtest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton // AppCompatButton 사용 권장
import androidx.fragment.app.Fragment
import com.example.gift4u.R

class GiftTestQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttestquestion, container, false)

        // 1. 버튼 초기화
        val btnA = view.findViewById<AppCompatButton>(R.id.btn_answer_a)
        val btnB = view.findViewById<AppCompatButton>(R.id.btn_answer_b)
        val btnC = view.findViewById<AppCompatButton>(R.id.btn_answer_c)

        // 리스트로 묶어서 관리
        val buttons = listOf(btnA, btnB, btnC)

        // 2. 각 버튼에 클릭 리스너 달기
        buttons.forEach { button ->
            button.setOnClickListener {
                // (1) 모든 버튼의 선택 상태 초기화 (하나만 선택되게 하기 위함)
                buttons.forEach { it.isSelected = false }

                // (2) 클릭한 버튼만 '선택됨' 상태로 변경 (보라색 배경 적용됨)
                button.isSelected = true

                // (3) 0.3초 딜레이 후 다음 화면으로 이동
                // (사용자가 자신이 뭘 눌렀는지 색깔 변화를 볼 시간을 줌)
                Handler(Looper.getMainLooper()).postDelayed({
                    moveToNextStep()
                }, 300)
            }
        }

        return view
    }

    private fun moveToNextStep() {
        // 프래그먼트가 아직 붙어있는지 확인 (앱 종료 시 크래시 방지)
        if (!isAdded) return

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, GiftTestResultFragment())
            .commit()
    }
}