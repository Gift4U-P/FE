package com.example.gift4u.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.api.login.model.LoginRequest
import com.example.gift4u.api.login.model.LoginResponse
import com.example.gift4u.api.login.repository.LoginRepository
import com.example.gift4u.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private val repository = LoginRepository() // 리포지토리 인스턴스

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val etEmail = view.findViewById<EditText>(R.id.et_login_email)
        val etPassword = view.findViewById<EditText>(R.id.et_login_password)
        val btnLogin = view.findViewById<AppCompatButton>(R.id.btn_login)
        val tvGoSignup = view.findViewById<TextView>(R.id.tv_go_signup)

        // 1. 로그인 버튼 클릭
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 로그인 요청 객체 생성
            val request = LoginRequest(email, password)

            // API 호출
            attemptLogin(request)
        }

        // 2. 회원가입 텍스트 클릭 -> 회원가입 화면으로 이동
        tvGoSignup.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, SignupFragment())
                .addToBackStack(null) // 뒤로가기 누르면 다시 로그인 화면으로 오게 설정
                .commit()
        }

        return view
    }

    private fun attemptLogin(request: LoginRequest) {
        repository.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val resp = response.body()

                // isSuccess 체크 및 result null 체크
                if (response.isSuccessful && resp != null && resp.isSuccess) {
                    val result = resp.result

                    if (result != null) {
                        // [중요] 토큰 저장 (RefreshToken을 AccessToken 대용으로 사용하는 경우)
                        Gift4uClient.currentAccessToken = result.refreshToken

                        Log.d("Login", "로그인 성공: ${result.name}, Token: ${result.refreshToken}")
                        Toast.makeText(context, "${result.name}님 환영합니다!", Toast.LENGTH_SHORT).show()

                        // 메인 화면으로 이동
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)

                        // 로그인 액티비티(AuthActivity) 종료 -> 뒤로가기 눌러도 로그인화면 안 나오게
                        activity?.finish()
                    } else {
                        Toast.makeText(context, "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 비즈니스 로직 실패 (비번 틀림 등) 또는 서버 에러
                    val msg = resp?.message ?: "로그인에 실패했습니다."
                    Log.e("Login", "실패: $msg (Code: ${response.code()})")
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login", "네트워크 오류", t)
                Toast.makeText(context, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}