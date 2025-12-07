package com.example.gift4u.ui.keyword

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.api.home.model.HomeGiftItem
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.gtest.model.Big5Result
import com.example.gift4u.api.ktest.model.KeywordSaveRequest
import com.example.gift4u.api.ktest.model.KeywordSaveResponse
import com.example.gift4u.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        view.findViewById<TextView>(R.id.tv_bubble_situation).text = keywords.getOrElse(3) { "" }



        // 2. API 결과 데이터 매핑
        val resultData = arguments?.getSerializable("keywordResult") as? Big5Result // Big5Result 재사용 중

        resultData?.let { data ->
            // 편지 문구 등 텍스트 설정
            view.findViewById<TextView>(R.id.tv_content_message_card).text = data.cardMessage

            // "20대 연인의 생일에는..." 문구 동적 변경
            view.findViewById<TextView>(R.id.tv_keywordText_recommend_label).text =
                "${keywords[0]} ${keywords[2]}의 ${keywords[3]}에는\n이런 선물을 추천해요!"

            // 추천 선물 리스트 설정
            val giftList = data.giftList.map {
                HomeGiftItem(
                    title = it.title,
                    lprice = it.lprice,
                    link = it.link,
                    image = it.image,
                    mallName = it.mallName
                )
            }

            val rvRecommend = view.findViewById<RecyclerView>(R.id.rv_result_recommend)
            rvRecommend.adapter = GiftAdapter(giftList)
            rvRecommend.layoutManager = GridLayoutManager(context, 3)
        }

        // 3. 결과 화면 하단 BNV (저장 / 홈으로) 설정
        val resultBnv = view.findViewById<BottomNavigationView>(R.id.result_bnv)

        resultBnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_result_save -> {
                    // 저장 다이얼로그 호출
                    showSaveDialog(requireContext())
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

    // 저장 다이얼로그
    private fun showSaveDialog(context: Context) {
        val inputField = EditText(context).apply {
            hint = "예: 20대 여친 생일 선물"
            setPadding(50, 50, 50, 50)
        }

        AlertDialog.Builder(context)
            .setTitle("키워드 결과 저장")
            .setMessage("저장할 이름을 입력해주세요.")
            .setView(inputField)
            .setPositiveButton("저장") { dialog, _ ->
                val name = inputField.text.toString().trim()
                if (name.isNotEmpty()) callSaveApi(name)
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
            .show()
    }

    // 저장 API 호출
    private fun callSaveApi(name: String) {
        val request = KeywordSaveRequest(savedName = name)

        Gift4uClient.keywordTestService.saveKeywordResult(request).enqueue(object :
            Callback<KeywordSaveResponse> {
            override fun onResponse(call: Call<KeywordSaveResponse>, response: Response<KeywordSaveResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "'$name' 저장 완료!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "저장 실패", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<KeywordSaveResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류", Toast.LENGTH_SHORT).show()
            }
        })
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