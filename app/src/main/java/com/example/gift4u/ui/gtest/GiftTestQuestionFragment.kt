package com.example.gift4u.ui.gtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.gift4u.R

class GiftTestQuestionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttestquestion, container, false)

        val btnA = view.findViewById<Button>(R.id.btn_answer_a)
        val btnB = view.findViewById<Button>(R.id.btn_answer_b)

        // 버튼 클릭 시 다음 화면(결과)으로 이동하게 임시 설정
        // 실제로는 질문 데이터를 교체하다가 마지막에 이동해야 함
        val nextStep = View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, GiftTestResultFragment())
                // .addToBackStack(null) // 결과 화면에서는 뒤로가기 막을거면 제거
                .commit()
        }

        btnA.setOnClickListener(nextStep)
        btnB.setOnClickListener(nextStep)

        return view
    }
}