package com.example.gift4u.api.gtest.model

data class Big5Question(
    val content: String,
    val optionA: String,
    val optionB: String,
    val optionC: String
)

val ALL_QUESTIONS = listOf(
    // Q1
    Big5Question(
        content = "그 사람은 새로운 사람들과 이야기하거나 모임에 참여하는 것을 좋아하는 편인가요?",
        optionA = "네, 즐기는 편입니다.",
        optionB = "아니요, 조용한 환경을 더 선호합니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q2
    Big5Question(
        content = "그 사람은 주말이나 휴일에 집보다 밖에서 활동하는 시간을 더 많이 보내나요?",
        optionA = "네, 활동적인 일정이 많습니다.",
        optionB = "아니요, 주로 집에서 쉬거나 조용한 활동을 합니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q3
    Big5Question(
        content = "그 사람은 주변 사람들의 감정이나 분위기를 잘 살피는 편인가요?",
        optionA = " 네, 다른 사람을 잘 배려합니다.",
        optionB = "아니요, 자신의 생각을 우선하는 편입니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q4
    Big5Question(
        content = " 그 사람은 갈등 상황이 발생하면 부드럽게 해결하려는 경향이 있나요?",
        optionA = "네, 가능한 한 평화적으로 해결하려 합니다.",
        optionB = "아니요, 직접적으로 의견을 표현하는 편입니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q5
    Big5Question(
        content = " 그 사람은 약속 시간이나 일정 관리를 꼼꼼하게 하는 편인가요?",
        optionA = "네, 계획을 잘 지키는 편입니다.",
        optionB = "아니요, 계획이나 일정에 유연한 편입니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q6
    Big5Question(
        content = "그 사람은 실용적이고 오래 쓸 수 있는 물건을 선호하나요?",
        optionA = "네, 기능성과 실용성을 중요하게 생각합니다.",
        optionB = "아니요, 실용성보다는 감성이나 디자인을 더 중시합니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q7
    Big5Question(
        content = "그 사람은 돌발 상황이나 일정 변경에 스트레스를 받는 편인가요?",
        optionA = "네, 변화에 예민한 편입니다.",
        optionB = "아니요, 변화에도 비교적 안정적입니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q8
    Big5Question(
        content = "그 사람은 감정 기복이 느껴질 때가 종종 있나요?",
        optionA = "네, 감정 변화가 비교적 잘 드러납니다.",
        optionB = "아니요, 감정 표현이 안정적입니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q9
    Big5Question(
        content = "그 사람은 새로운 취미나 활동을 시도하는 것을 좋아하나요?",
        optionA = "네, 새로운 경험을 즐깁니다.",
        optionB = "아니요, 익숙한 방식과 루틴을 더 좋아합니다.",
        optionC = "잘 모르겠습니다."
    ),
    // Q10
    Big5Question(
        content = "그 사람은 독특한 아이디어나 감각적인 디자인의 물건을 좋아하나요?",
        optionA = "네, 개성 있는 스타일을 선호합니다.",
        optionB = "아니요, 단정하고 무난한 스타일을 더 선호합니다.",
        optionC = "잘 모르겠습니다."
    )
)
