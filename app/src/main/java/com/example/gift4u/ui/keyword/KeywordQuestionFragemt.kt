package com.example.gift4u.ui.keyword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.Gift4uClient.keywordTestService
import com.example.gift4u.api.ktest.model.KeywordResultRequest
import com.example.gift4u.api.ktest.model.KeywordResultResponse
import com.example.gift4u.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

// 질문 데이터 클래스
data class QuestionStep(
    val title: String,
    val subtitle: String,
    val options: List<String>
)

class KeywordQuestionFragment : Fragment() {

    private var currentStep = 0
    private val selectedKeywords = mutableListOf<String>() // 선택한 답 저장용

    // 4단계 질문 데이터 정의
    private val questions = listOf(
        QuestionStep("나이", "선물하려는 상대방의\n나이대는 어떻게 되나요?", listOf("10대", "20대", "30대", "40대", "50대", "60대 이상")),
        QuestionStep("성별", "선물하려는 상대방의 성별은\n어떻게 되나요?", listOf("남성", "여성")),
        QuestionStep("관계", "선물하려는 상대방과\n어떤 관계인가요?", listOf("가족", "연인", "친구", "동료", "사제", "기타")),
        QuestionStep("상황", "어떤 상황에\n선물하시나요?", listOf("생일", "합격축하", "졸업", "취업", "승진", "기념일", "집들이", "출산", "감사", "응원"))
    )

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var rvOptions: RecyclerView
    // 진행 상태 표시
    private lateinit var tvProgress: TextView
    private lateinit var adapter: OptionAdapter

    // 로딩 뷰 변수 선언
    private lateinit var loadingLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keyword_question, container, false)

        // 로딩 레이아웃 연결
        loadingLayout = view.findViewById(R.id.layout_loading)

        // Bottom Navigation 숨기기
        (activity as? MainActivity)?.setBottomNavVisibility(false)

        tvTitle = view.findViewById(R.id.tv_question_title)
        tvSubtitle = view.findViewById(R.id.tv_question_subtitle)
        rvOptions = view.findViewById(R.id.rv_options)
        tvProgress = view.findViewById(R.id.tv_keyword_progress)


        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            if (currentStep > 0) {
                currentStep--
                selectedKeywords.removeAt(selectedKeywords.lastIndex)
                loadQuestion()
            } else {
                parentFragmentManager.popBackStack()
            }
        }

        // RecyclerView 설정 (2열 그리드)
        rvOptions.layoutManager = GridLayoutManager(context, 2)
        adapter = OptionAdapter { selectedOption ->
            onOptionSelected(selectedOption)
        }
        rvOptions.adapter = adapter

        loadQuestion() // 첫 질문 로드
        return view
    }

    private fun loadQuestion() {
        val question = questions[currentStep]
        tvTitle.text = question.title
        tvSubtitle.text = question.subtitle
        // 진행 상태 TextView 업데이트 ("N/총 개수" 형식)
        tvProgress.text = "${currentStep + 1}/${questions.size}"
        adapter.submitList(question.options)
    }

    private fun onOptionSelected(option: String) {
        selectedKeywords.add(option)

        // 0.3초 딜레이 후 다음 단계로
        Handler(Looper.getMainLooper()).postDelayed({
            if (currentStep < questions.size - 1) {
                currentStep++
                loadQuestion()
            } else {
                // 마지막 질문 완료 시 API 호출
                sendKeywordResults()
            }
        }, 300)
    }

    // 키워드 결과 전송 API 호출
    private fun sendKeywordResults() {
        // 선택된 키워드: [나이, 성별, 관계, 상황]
        // 예: ["20대", "여성", "연인", "생일"] -> API 요청 모델로 변환

        // API 호출 시작 전 로딩 화면 보이기
        loadingLayout.visibility = View.VISIBLE

        val age = selectedKeywords.getOrElse(0) { "20대" }
        val gender = selectedKeywords.getOrElse(1) { "여성" }
        val relationship = selectedKeywords.getOrElse(2) { "친구" }
        val situation = selectedKeywords.getOrElse(3) { "생일" }

        val request = KeywordResultRequest(
            age = age,
            gender = gender,
            relationship = relationship,
            situation = situation
        )

        keywordTestService.sendKeywordResults(request).enqueue(object :
            Callback<KeywordResultResponse> {
            override fun onResponse(call: Call<KeywordResultResponse>, response: Response<KeywordResultResponse>) {

                // 응답 받으면 로딩 화면 숨김
                loadingLayout.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val resultData = response.body()!!.result

                    // 성공 -> 결과 화면으로 이동
                    val fragment = KeywordResultFragment()
                    val bundle = Bundle()
                    bundle.putStringArrayList("keywords", ArrayList(selectedKeywords)) // 상단 버블용
                    bundle.putSerializable("keywordResult", resultData) // API 결과 전달
                    fragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.main_fragmentContainer, fragment)
                        .commit()
                } else {
                    Log.e("KeywordTest", "Failed: ${response.code()}")
                    Toast.makeText(context, "결과 분석에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<KeywordResultResponse>, t: Throwable) {
                Log.e("KeywordTest", "Network Error", t)
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 나갈 때 Bottom Nav 다시 보이기
        (activity as? MainActivity)?.setBottomNavVisibility(true)
    }
}

class OptionAdapter(private val onClick: (String) -> Unit) : RecyclerView.Adapter<OptionAdapter.ViewHolder>() {
    private var items = listOf<String>()
    private var selectedPosition = -1

    fun submitList(newItems: List<String>) {
        items = newItems
        selectedPosition = -1 // 초기화
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: AppCompatButton = view.findViewById(R.id.btn_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_keyword_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item

        // 선택 상태 관리
        holder.button.isSelected = (position == selectedPosition)

        holder.button.setOnClickListener {
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged() // UI 갱신 (색상 변경)
            onClick(item)
        }
    }

    override fun getItemCount() = items.size
}