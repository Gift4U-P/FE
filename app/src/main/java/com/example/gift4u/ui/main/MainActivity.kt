package com.example.gift4u.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gift4u.R
import com.example.gift4u.api.Gift4uClient
import com.example.gift4u.ui.gtest.GiftTestFragment
import com.example.gift4u.ui.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 토큰 확인 로그
        val token = Gift4uClient.currentAccessToken
        if (token != null) {
            Log.d("TokenCheck", "MainActivity 진입 성공! 토큰: $token")
        } else {
            Log.e("TokenCheck", "MainActivity 진입했으나 토큰이 없음 (비로그인 상태 혹은 오류)")
        }

        // 1. 하단 바 연결
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.main_bnv)

        // 2. 앱 실행 시 처음에 보여줄 프래그먼트 설정 (HomeFragment)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragmentContainer, HomeFragment())
                .commit()
        }

        // 3. 하단 버튼 클릭 리스너 설정
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    // 홈 버튼 클릭 시 HomeFragment로 교체
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.recommendFragment -> {
                    replaceFragment(GiftTestFragment())
                    true
                }
                R.id.shopFragment -> {
                    // replaceFragment(ChatFragment())
                    true
                }
                R.id.mypageFragment -> {
                    replaceFragment(MyPageFragment())
                    true
                }
                else -> false
            }
        }
    }

    // 프래그먼트 교체
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragmentContainer, fragment)
            .commit()
    }

    // bnv visibility 설정
    fun setBottomNavVisibility(visible: Boolean) {
        val bottomNavContainer = findViewById<androidx.cardview.widget.CardView>(R.id.bottom_nav_container)
        bottomNavContainer.visibility = if (visible) View.VISIBLE else View.GONE
    }
}