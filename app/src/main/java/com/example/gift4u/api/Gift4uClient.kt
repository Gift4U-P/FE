package com.example.gift4u.api

import com.example.gift4u.BuildConfig
import com.example.gift4u.api.gtest.network.GiftTestService
import com.example.gift4u.api.home.network.HomeService
import com.example.gift4u.api.ktest.network.KeywordTestService
import com.example.gift4u.api.login.network.LoginService
import com.example.gift4u.api.mypage.network.MyPageService

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Gift4uClient {

    // Swagger에서 가져오는 주소
    private const val BASE_URL = BuildConfig.BASE_URL

    //일단 호출을 로그로 추적하게
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Volatile // 여러 스레드에서 접근할 수 있으므로 volatile 키워드 추가
    var currentAccessToken: String? = null // 로그인 성공 후 여기에 토큰 저장

    // 인증 헤더를 추가하는 인터셉터
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()

        // currentAccessToken이 null이 아니면 Authorization 헤더 추가
        currentAccessToken?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }

        val request = builder.build()
        chain.proceed(request)
    }

    //클라이언트(각 연결/쓰기/읽기 작업에서 최대 60초 대기)
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor) // 인증 인터셉터 추가
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //API service(network)를 구현하고 밑에 추가함으로써 유지보수 용이하게
    //로그인 관련
    val loginService: LoginService = retrofit.create(LoginService::class.java)

    // 홈
    val homeService: HomeService = retrofit.create(HomeService::class.java)

    // 마이페이지
    val myPageService: MyPageService = retrofit.create(MyPageService::class.java)

    // Big5 결과
    val giftTestService: GiftTestService = retrofit.create(GiftTestService::class.java)

    // 키워드 결과
    val keywordTestService: KeywordTestService = retrofit.create(KeywordTestService::class.java)

}