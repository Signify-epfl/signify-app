package com.github.se.signify.model.quiz

interface QuizRepository {
    fun init(onSuccess: () -> Unit)

    fun getQuizQuestions(onSuccess: (List<QuizQuestion>) -> Unit, onFailure: (Exception) -> Unit)
}