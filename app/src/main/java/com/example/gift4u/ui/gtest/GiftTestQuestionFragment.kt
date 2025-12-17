package com.example.gift4u.ui.gtest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton // AppCompatButton 사용 권장
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import android.widget.TextView
import android.widget.Toast
import com.example.gift4u.api.gtest.network.GiftTestService
import com.example.gift4u.api.Big5TestState
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.gtest.model.ALL_QUESTIONS
import com.example.gift4u.api.gtest.model.Big5ResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 20개의 질문을 미리 정의
class GiftTestQuestionFragment : Fragment() {

    // 현재 프래그먼트가 몇 번째 질문인지 추적
    private var questionIndex: Int = 0

    // 로딩 뷰 변수 선언
    private lateinit var loadingLayout: View

    // 서비스 객체 초기화 (API 호출에 사용)
    private val giftTestService: GiftTestService by lazy {
        Gift4uClient.retrofit.create(GiftTestService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gifttestquestion, container, false)

        // 1. 버튼 초기화
        val btnA = view.findViewById<AppCompatButton>(R.id.btn_answer_a)
        val btnB = view.findViewById<AppCompatButton>(R.id.btn_answer_b)
        val btnC = view.findViewById<AppCompatButton>(R.id.btn_answer_c)

        // 로딩 레이아웃 연결
        loadingLayout = view.findViewById(R.id.layout_loading)

        // 추후 진행상황 표시 필요하다면 쓰기
        questionIndex = arguments?.getInt("questionIndex", 0) ?: 0

        // 크래시 방어(questionIndex가 리스트 크기(10)와 같거나 크면, 크래시를 방지하고 프래그먼트 생성을 중단.)
        if (questionIndex >= ALL_QUESTIONS.size) {
            Log.e("GTest", "Error: Question index out of bounds (${questionIndex}). Should have moved to Result Fragment.")
            // 이 상황은 API 호출 후 최종 Result Fragment로 이동했어야 하므로, 빈 뷰 반환 후 종료
            return View(context)
        }

        val currentQuestion = ALL_QUESTIONS[questionIndex] // 현재 질문 데이터 로드

        // 질문 번호 TextView 초기화
        val tvQuestionNum = view.findViewById<TextView>(R.id.tv_question_num)

        // 질문 번호 매칭: 인덱스(0부터)에 1을 더해 Q1., Q2. 형식으로 표시
        tvQuestionNum.text = "Q${questionIndex + 1}."

        // 현재 질문 내용 설정
        val tvContent = view.findViewById<TextView>(R.id.tv_question_content)
        tvContent.text = currentQuestion.content

        btnA.text = currentQuestion.optionA
        btnB.text = currentQuestion.optionB
        btnC.text = currentQuestion.optionC

        // 리스트로 묶어서 관리
        val buttons = listOf(btnA, btnB, btnC)

        // 2. 각 버튼에 클릭 리스너 달기
        buttons.forEach { button ->
            button.setOnClickListener {
                // (1) 모든 버튼의 선택 상태 초기화 (하나만 선택되게 하기 위함)
                buttons.forEach { it.isSelected = false }

                // (2) 클릭한 버튼만 '선택됨' 상태로 변경 (보라색 배경 적용됨)
                button.isSelected = true

                // 답변 값 추출
                val answerValue = when(button.id) {
                    R.id.btn_answer_a -> "A"
                    R.id.btn_answer_b -> "B"
                    R.id.btn_answer_c -> "C"
                    else -> ""
                }

                // 답변을 Big5TestState에 저장
                Big5TestState.saveAnswer(answerValue)

                // (3) 0.3초 딜레이 후 다음 화면으로 이동
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

        // 현재까지 저장된 답변 개수 = 다음에 로드할 질문의 인덱스 (1부터 10까지)
        val nextIndex = Big5TestState.answers.size

        if (nextIndex < ALL_QUESTIONS.size) {
            // 다음 질문 로드 (nextIndex가 0~9일 경우 실행)
            val nextFragment = GiftTestQuestionFragment().apply {
                arguments = Bundle().apply {
                    putInt("questionIndex", nextIndex)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, nextFragment)
                .addToBackStack(null)
                .commit()
        } else {
            // 모든 질문(10개)이 완료되었을 경우 (nextIndex가 10일 때 실행)
            sendResultsAndMoveToFinalResult()
        }
    }

    private fun sendResultsAndMoveToFinalResult() {
        val request = Big5TestState.buildRequest()

        // API 호출 시작 전 로딩 화면 보이기
        loadingLayout.visibility = View.VISIBLE

        giftTestService.sendBig5Results(request).enqueue(object :
            Callback<Big5ResultResponse> {
            override fun onResponse(call: Call<Big5ResultResponse>, response: Response<Big5ResultResponse>) {

                // 응답 받으면 로딩 화면 숨김
                loadingLayout.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val resultData = response.body()!!.result

                    // 성공 -> 결과 프래그먼트로 데이터 전달 및 이동
                    val fragment = GiftTestResultFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable("big5Result", resultData) // 데이터를 Result Fragment로 넘김
                        }
                    }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, fragment)
                        .commit()

                    Big5TestState.reset() // 테스트 완료 후 상태 초기화
                } else {
                    Toast.makeText(context, "결과 분석에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Big5ResultResponse>, t: Throwable) {
                Toast.makeText(context, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}