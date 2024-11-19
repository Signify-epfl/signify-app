package com.github.se.signify.model.quiz

data class QuizQuestion(
    val correctWord: String = "",
    val signs: List<Int> = emptyList(), // List of image names or URLs
    val confusers: List<String> = emptyList()
)

