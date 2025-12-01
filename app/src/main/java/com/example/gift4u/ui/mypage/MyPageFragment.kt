package com.example.gift4u.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gift4u.R
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.mypage.model.SurveyResultListResponse
import com.example.gift4u.adaptor.MyPageAdapter
import com.example.gift4u.api.mypage.model.SurveyResultItem
import com.example.gift4u.api.mypage.model.UserProfileResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {

    private lateinit var rvList: RecyclerView
    private lateinit var adapter: MyPageAdapter
    private lateinit var tvEmpty: TextView

    // 프로필 UI 요소
    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserGender: TextView

    // 탭 UI 요소
    private lateinit var tvBig5: TextView
    private lateinit var lineBig5: View
    private lateinit var tvKeyword: TextView
    private lateinit var lineKeyword: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 1. 뷰 초기화
        rvList = view.findViewById(R.id.rv_mypage_list)
        tvEmpty = view.findViewById(R.id.tv_empty_list)

        // 프로필 뷰 초기화
        tvUserName = view.findViewById(R.id.tv_user_name)
        tvUserEmail = view.findViewById(R.id.tv_myPage_email)
        tvUserGender = view.findViewById(R.id.tv_myPage_gender)

        tvBig5 = view.findViewById(R.id.tv_tab_big5)
        lineBig5 = view.findViewById(R.id.indicator_big5)
        tvKeyword = view.findViewById(R.id.tv_tab_keyword)
        lineKeyword = view.findViewById(R.id.indicator_keyword)

        // 리싸이클러뷰 설정
        rvList.layoutManager = LinearLayoutManager(context)
        adapter = MyPageAdapter(emptyList())
        rvList.adapter = adapter

        // 2. 탭 클릭 리스너
        view.findViewById<LinearLayout>(R.id.tab_big5).setOnClickListener {
            setTabSelected(isBig5 = true)
            fetchBig5Results() // Big5 데이터 로드
        }

        view.findViewById<LinearLayout>(R.id.tab_keyword).setOnClickListener {
            setTabSelected(isBig5 = false)
            fetchKeywordResults()
        }

        // 3. 초기 상태설정 및 데이터 로드
        fetchUserProfile() // 프로필 정보 불러오기

        setTabSelected(isBig5 = true)
        fetchBig5Results()

        // 뒤로가기 버튼 리스너
        val btnBack: ImageView = view.findViewById(R.id.btn_back)
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return view
    }

    // 탭 UI 변경 함수
    private fun setTabSelected(isBig5: Boolean) {
        val selectedColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val unselectedColor = Color.parseColor("#AAAAAA")

        if (isBig5) {
            tvBig5.setTextColor(selectedColor)
            lineBig5.visibility = View.VISIBLE
            tvKeyword.setTextColor(unselectedColor)
            lineKeyword.visibility = View.INVISIBLE
        } else {
            tvBig5.setTextColor(unselectedColor)
            lineBig5.visibility = View.INVISIBLE
            tvKeyword.setTextColor(selectedColor)
            lineKeyword.visibility = View.VISIBLE
        }
    }

// API 호출

    // [추가] 사용자 프로필 API 호출
    private fun fetchUserProfile() {
        Gift4uClient.myPageService.getUserProfile().enqueue(object : Callback<UserProfileResponse> {
            override fun onResponse(
                call: Call<UserProfileResponse>,
                response: Response<UserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        val result = resp.result

                        // 데이터 바인딩
                        result?.let {
                            tvUserName.text = "${it.name} 님" // 예: "홍길동님"
                            tvUserEmail.text = it.email

                            // 성별 변환 (MALE -> 남, FEMALE -> 여)
                            tvUserGender.text = when(it.gender) {
                                "MALE" -> "남"
                                "FEMALE" -> "여"
                                else -> "기타"
                            }
                        }
                    }
                } else {
                    Log.e("MyPage", "Profile Load Failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                Log.e("MyPage", "Profile Network Error", t)
            }
        })
    }
// Big5 API 호출
    private fun fetchBig5Results() {
        Gift4uClient.myPageService.getSurveyResults().enqueue(object : Callback<SurveyResultListResponse> {
            override fun onResponse(
                call: Call<SurveyResultListResponse>,
                response: Response<SurveyResultListResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        val wrapper = resp.result
                        val list = wrapper?.surveyList ?: emptyList()

                        Log.d("MyPageDebug", "Big5 데이터 개수: ${list.size}")

                        if (list.isEmpty()) {
                            tvEmpty.text = "저장된 결과가 없습니다."
                            tvEmpty.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        } else {
                            tvEmpty.visibility = View.GONE
                            rvList.visibility = View.VISIBLE
                            adapter.updateList(list, "Big5")
                        }
                    }
                } else {
                    Log.e("MyPage", "Big5 Load Failed: ${response.code()}")
                    Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SurveyResultListResponse>, t: Throwable) {
                Log.e("MyPage", "Network Error", t)
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 키워드 추천 API 호출
    private fun fetchKeywordResults() {
        Gift4uClient.myPageService.getKeywordResults().enqueue(object : Callback<SurveyResultListResponse> {
            override fun onResponse(
                call: Call<SurveyResultListResponse>,
                response: Response<SurveyResultListResponse>
            ) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null && resp.isSuccess) {
                        val list = resp.result?.surveyList ?: emptyList()

                        Log.d("MyPageDebug", "Keyword 데이터 개수: ${list.size}")

                        if (list.isEmpty()) {
                            tvEmpty.text = "저장된 키워드 결과가 없습니다."
                            tvEmpty.visibility = View.VISIBLE
                            rvList.visibility = View.GONE
                        } else {
                            tvEmpty.visibility = View.GONE
                            rvList.visibility = View.VISIBLE
                            adapter.updateList(list, "키워드")
                        }
                    }
                } else {
                    Log.e("MyPage", "Keyword Load Failed: ${response.code()}")
                    Toast.makeText(context, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SurveyResultListResponse>, t: Throwable) {
                Log.e("MyPage", "Keyword Network Error", t)
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}