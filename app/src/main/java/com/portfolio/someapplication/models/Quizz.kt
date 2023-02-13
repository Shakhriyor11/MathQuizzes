package com.portfolio.someapplication.models

data class Quizz(
    var id: String = "",
    var title: String = "",
    var questions: MutableMap<String, Question> = mutableMapOf()
)