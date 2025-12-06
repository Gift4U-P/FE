package com.example.gift4u.api

import com.example.gift4u.api.gtest.model.Big5ResultRequest

object Big5TestState {
    var answers = mutableMapOf<String, String>()

    fun reset() {
        answers.clear()
    }

    val questionKeys = listOf(
        "qone", "qtwo", "qthree", "qfour", "qfive", "qsix", "qseven", "qeight", "qnine", "qten",
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
            qone = answers["qone"] ?: "",
            qtwo = answers["qtwo"] ?: "",
            qthree = answers["qthree"] ?: "",
            qfour = answers["qfour"] ?: "",
            qfive = answers["qfive"] ?: "",
            qsix = answers["qsix"] ?: "",
            qseven = answers["qseven"] ?: "",
            qeight = answers["qeight"] ?: "",
            qnine = answers["qnine"] ?: "",
            qten = answers["qten"] ?: ""
        )
    }
}