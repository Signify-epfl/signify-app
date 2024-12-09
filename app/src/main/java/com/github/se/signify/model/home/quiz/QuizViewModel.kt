package com.github.se.signify.model.home.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.common.annotations.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

  // State for the list of all quizzes
  private val quizzes_ = MutableStateFlow<List<QuizQuestion>>(emptyList())
  val quizzes: StateFlow<List<QuizQuestion>> = quizzes_.asStateFlow()

  // State for the current quiz
  private val currentQuiz_ = MutableStateFlow<QuizQuestion?>(null)
  val currentQuiz: StateFlow<QuizQuestion?> = currentQuiz_.asStateFlow()

  // Expose for testing
  @VisibleForTesting
  val currentQuizTesting: MutableStateFlow<QuizQuestion?>
    get() = currentQuiz_

  @VisibleForTesting
  val quizzesTesting: MutableStateFlow<List<QuizQuestion>>
    get() = quizzes_

  init {
    repository.init { getQuizQuestions() }
  }

  companion object {
    fun factory(repository: QuizRepository): ViewModelProvider.Factory {
      return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
          return QuizViewModel(repository) as T
        }
      }
    }
  }

  fun getQuizQuestions() {
    repository.getQuizQuestions(
        onSuccess = { quizList ->
          quizzes_.value = quizList
          selectRandomQuiz() // Select a random quiz immediately after fetching
        },
        onFailure = {})
  }

  fun submitAnswer(selectedOption: String, onCorrect: () -> Unit, onIncorrect: () -> Unit) {
    val quiz = currentQuiz_.value
    if (quiz != null && selectedOption == quiz.correctWord) {
      onCorrect()
      selectRandomQuiz() // Select a new random quiz after a correct answer
    } else {
      onIncorrect()
    }
  }

  private fun selectRandomQuiz() {
    if (quizzes_.value.isNotEmpty()) {
      currentQuiz_.value = quizzes_.value.random() // Select a random quiz
    } else {
      currentQuiz_.value = null // Handle the case where there are no quizzes
    }
  }
}
