package com.example.gift4u.api

import com.example.gift4u.api.gtest.model.Big5ResultRequest

object Big5TestState {
    var answers = mutableMapOf<String, String>()

    fun reset() {
        answers.clear()
    }

    val questionKeys = listOf(
        "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10",
    )

    fun getCurrentKey(): String {
        return questionKeys[answers.size]
    }

    fun getTotalQuestions(): Int = questionKeys.size

    fun saveAnswer(answerValue: String) {
        val currentKey = getCurrentKey()
        answers[currentKey] = answerValue
    }

    fun isLastQuestion(): Boolean {
        return answers.size == getTotalQuestions() - 1
    }

    fun buildRequest(): Big5ResultRequest {
        // 10개 답변을 Big5ResultRequest 모델로 매핑하여 반환
        return Big5ResultRequest(
            q1 = answers["q1"] ?: "",
            q2 = answers["q2"] ?: "",
            q3 = answers["q3"] ?: "",
            q4 = answers["q4"] ?: "",
            q5 = answers["q5"] ?: "",
            q6 = answers["q6"] ?: "",
            q7 = answers["q7"] ?: "",
            q8 = answers["q8"] ?: "",
            q9 = answers["q9"] ?: "",
            q10 = answers["q10"] ?: ""
        )
    }
}