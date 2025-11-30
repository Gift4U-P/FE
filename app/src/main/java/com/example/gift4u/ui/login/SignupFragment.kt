package com.example.gift4u.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import com.example.gift4u.api.login.model.SignupRequest
import com.example.gift4u.api.login.model.SignupResponse
import com.example.gift4u.api.login.repository.LoginRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment : Fragment() {

    private val repository = LoginRepository() // 리포지토리 생성

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃 연결 (파일명이 fragment_signup.xml이라고 가정)
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // View 초기화
        val etName = view.findViewById<EditText>(R.id.et_name)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val rgGender = view.findViewById<RadioGroup>(R.id.rg_gender)
        val btnSignup = view.findViewById<AppCompatButton>(R.id.btn_signup_complete)

        // 가입하기 버튼 클릭 리스너
        btnSignup.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // 1. 입력값 유효성 검사 (빈칸 체크)
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. 성별 확인
            val genderId = rgGender.checkedRadioButtonId
            if (genderId == -1) {
                Toast.makeText(context, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // API 명세에 맞춰 "MALE" 또는 "FEMALE" 문자열로 변환
            val gender = when (genderId) {
                R.id.rb_male -> "MALE"
                R.id.rb_female -> "FEMALE"
                else -> "MALE" // 기본값 (혹은 에러 처리)
            }

            // 3. 요청 객체 생성
            val request = SignupRequest(
                name = name,
                gender = gender,
                email = email,
                password = password
            )

            // 4. API 통신 호출
            signUp(request)
        }

        return view
    }

    private fun signUp(request: SignupRequest) {
        repository.signUp(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("Signup", "가입 성공: ID=${result?.userId}, Name=${result?.name}")

                    Toast.makeText(context, "회원가입에 성공했습니다!", Toast.LENGTH_SHORT).show()

                    // 가입 성공 시 로그인 화면으로 이동하거나 액티비티 종료
                    // 예: 프래그먼트 백스택에서 제거하여 이전 화면(로그인)으로 돌아가기
                    parentFragmentManager.popBackStack()

                } else {
                    // 서버 에러 (4xx, 5xx)
                    // 에러 메시지가 있다면 파싱해서 보여줄 수도 있음
                    Log.e("Signup", "가입 실패 코드: ${response.code()}")
                    Toast.makeText(context, "회원가입 실패: 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                // 통신 실패 (인터넷 끊김 등)
                Log.e("Signup", "통신 오류", t)
                Toast.makeText(context, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}