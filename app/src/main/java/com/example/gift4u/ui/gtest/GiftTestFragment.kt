package com.example.gift4u.ui.gtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.gift4u.R

class GiftTestFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttest, container, false)

        val btnStart = view.findViewById<Button>(R.id.btn_start)
        btnStart.setOnClickListener {
            // 질문 화면(TestQuestionFragment)으로 이동
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, GiftTestQuestionFragment())
                .addToBackStack(null) // 뒤로가기 가능하게
                .commit()
        }

        return view
    }
}