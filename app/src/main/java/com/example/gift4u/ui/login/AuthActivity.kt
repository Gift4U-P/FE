package com.example.gift4u.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gift4u.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // 앱 실행 시 처음에 '로그인 화면(LoginFragment)'을 띄워줌
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.auth_fragment_container, LoginFragment())
                .commit()
        }
    }
}