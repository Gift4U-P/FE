package com.example.gift4u.ui.gtest

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.adaptor.Big5EvidenceAdapter
import com.example.gift4u.adaptor.GiftAdapter
import com.example.gift4u.adaptor.ResultGiftAdapter
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.gift4u.api.gtest.model.Big5GiftItem
import com.example.gift4u.api.gtest.model.Big5Result
import com.example.gift4u.api.gtest.model.SurveySaveRequest
import com.example.gift4u.api.gtest.model.SurveySaveResponse
import com.example.gift4u.api.gtest.network.GiftTestService
import com.example.gift4u.api.home.model.HomeGiftItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GiftTestResultFragment : Fragment() {

    // [수정] XML의 콘텐츠 ID에 맞게 변수명 및 선언 변경
    private lateinit var tvAnalysisContent: TextView
    private lateinit var tvReasoningContent: TextView
    private lateinit var tvMessageContent: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttestresult, container, false)

        // 1. API 응답 데이터 받기 및 UI 요소 연결
        val resultData = arguments?.getSerializable("big5Result") as? Big5Result

        // UI 요소 연결 (XML Content ID 매칭)
        tvAnalysisContent = view.findViewById(R.id.tv_content_personality_analysis)
        tvReasoningContent = view.findViewById(R.id.tv_content_recommend_reasoning)
        tvMessageContent = view.findViewById(R.id.tv_content_message_card)

        resultData?.let { data ->
            // 분석 결과 내용
            tvAnalysisContent.text = data.analysis

            // 추천 이유 내용
            tvReasoningContent.text = data.reasoning

            // 편지 문구 내용
            tvMessageContent.text = data.cardMessage

            // 2. 선물 추천 리스트 처리
            val giftList = data.giftList

            // 상세 분석 근거(Evidence) RVA 연결
            val rvEvidence = view.findViewById<RecyclerView>(R.id.rv_evidence_list)
            rvEvidence.adapter = Big5EvidenceAdapter(data.evidence)
            rvEvidence.layoutManager = LinearLayoutManager(context)

            // GiftList 전체 항목을 GiftAdapter가 사용하는 Gift 객체로 변환
            val fullList = data.giftList.map { item ->
                HomeGiftItem(
                    title = item.title,
                    lprice = item.lprice,
                    link = item.link,
                    image = item.image,
                    mallName = item.mallName,
                    accuracy = item.accuracy
                )
            }.sortedByDescending { it.accuracy } // 높은 점수 순 정렬

            // 리스트 분리 (Best 3 vs 나머지)
            val bestList = fullList.take(3) // 상위 3개
            val restList = fullList.drop(3) // 나머지

            // Best 3 리사이클러뷰 설정 (박스형, Linear)
            val rvBest = view.findViewById<RecyclerView>(R.id.rv_gift_best)
            rvBest.adapter = ResultGiftAdapter(bestList, ResultGiftAdapter.TYPE_RANK)
            rvBest.layoutManager = LinearLayoutManager(context) // 세로 리스트

            // 나머지 리사이클러뷰 설정 (그리드형, Grid 3열)
            val rvRest = view.findViewById<RecyclerView>(R.id.rv_gift_rest)
            rvRest.adapter = ResultGiftAdapter(restList, ResultGiftAdapter.TYPE_GRID)
            rvRest.layoutManager = GridLayoutManager(context, 3) // 3칸 그리드

        } ?: run {
            // 데이터가 없을 경우 처리
            tvAnalysisContent.text = "분석 데이터를 불러오지 못했습니다."
        }


        // 3. 결과 화면 전용 BNV 설정
        val resultBnv = view.findViewById<BottomNavigationView>(R.id.result_bnv)
        resultBnv.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.menu_result_save -> {
                    // 저장 버튼 클릭 시 팝업 호출
                    showSaveNameDialog(requireContext())
                    true
                }
                R.id.menu_result_home -> {
                    // 홈으로 이동 로직
                    (activity as? MainActivity)?.let { mainActivity ->
                        mainActivity.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
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
    // 결과 저장 로직
    // 저장 이름 입력 다이얼로그 표시 함수
    private fun showSaveNameDialog(context: Context) {
        val inputField = EditText(context).apply {
            hint = "예: 친구 A의 Big5 분석 결과"
            setPadding(50, 50, 50, 50)
        }

        AlertDialog.Builder(context)
            .setTitle("분석 결과 저장")
            .setMessage("이 결과에 저장할 이름을 입력해주세요.")
            .setView(inputField)
            .setPositiveButton("저장") { dialog, _ ->
                val savedName = inputField.text.toString().trim()
                if (savedName.isNotEmpty()) {
                    callSaveApi(savedName) // API 호출
                } else {
                    Toast.makeText(context, "저장할 이름을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    // 결과 저장 API 호출 함수
    private fun callSaveApi(savedName: String) {
        val request = SurveySaveRequest(savedName = savedName)

        // Gift4uClient에 미리 정의해둔 giftTestService 사용
        val call = Gift4uClient.giftTestService.saveSurveyResult(request)

        call.enqueue(object : Callback<SurveySaveResponse> {
            override fun onResponse(call: Call<SurveySaveResponse>, response: Response<SurveySaveResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(context, "'${savedName}'(으)로 마이페이지에 저장되었습니다!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "저장 실패: 서버 응답 오류 (${response.code()})", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SurveySaveResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류로 저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
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