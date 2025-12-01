package com.example.gift4u.ui.keyword

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.ui.main.MainActivity

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
        QuestionStep("성별", "상대방의 성별은\n어떻게 되나요?", listOf("남성", "여성")),
        QuestionStep("관계", "상대방과\n어떤 관계인가요?", listOf("가족", "연인", "친구", "동료", "사제", "기타")),
        QuestionStep("분위기", "상대방의 분위기는\n어떤가요?", listOf("세련된", "귀여운", "차분한", "성숙한", "러블리한", "시크한", "활발한", "우아한"))
    )

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var rvOptions: RecyclerView
    // [수정] ProgressBar 대신 진행 상태를 표시할 TextView
    private lateinit var tvProgress: TextView
    private lateinit var adapter: OptionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_keyword_question, container, false)

        // Bottom Navigation 숨기기
        (activity as? MainActivity)?.setBottomNavVisibility(false)

        tvTitle = view.findViewById(R.id.tv_question_title)
        tvSubtitle = view.findViewById(R.id.tv_question_subtitle)
        rvOptions = view.findViewById(R.id.rv_options)
        // [수정] TextView 초기화
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
        // [수정] 진행 상태 TextView 업데이트 ("N/총 개수" 형식)
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
                moveToResult()
            }
        }, 300)
    }

    private fun moveToResult() {
        val fragment = KeywordResultFragment()
        val bundle = Bundle()
        // 결과 화면으로 선택한 키워드들을 전달 (예: "20대", "여성", "연인", "세련된")
        bundle.putStringArrayList("keywords", ArrayList(selectedKeywords))
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, fragment) // ID는 실제 Activity의 컨테이너 ID로 변경
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 나갈 때 Bottom Nav 다시 보이기
        (activity as? MainActivity)?.setBottomNavVisibility(true)
    }
}

// ... OptionAdapter 클래스는 그대로 유지됩니다. ...
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