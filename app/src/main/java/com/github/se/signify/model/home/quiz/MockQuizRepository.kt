package com.github.se.signify.model.home.quiz

import com.github.se.signify.model.common.getIconResId

private val failureException: Exception = Exception("Simulated failure")

class MockQuizRepository : QuizRepository {

  // Internal state to simulate failure or success
  private var shouldFail: Boolean = false

  // Mocked data for quiz questions
  private val mockedQuizQuestions =
      listOf(
          QuizQuestion(
              correctWord = "apple",
              signs =
                  listOf(
                      getIconResId('a'),
                      getIconResId('p'),
                      getIconResId('p'),
                      getIconResId('l'),
                      getIconResId('e')),
              confusers = listOf("juice", "fruit", "grape")))

  // Function to simulate a success scenario
  fun succeed() {
    shouldFail = false
  }

  // Function to simulate a failure scenario
  fun fail() {
    shouldFail = true
  }

  // Initialize repository
  override fun init(onSuccess: () -> Unit) {
    if (!checkFailure {}) return
    onSuccess()
  }

  override fun getQuizQuestions(
      onSuccess: (List<QuizQuestion>) -> Unit,
      onFailure: (Exception) -> Unit
  ) {
    if (!checkFailure(onFailure)) return
    onSuccess(mockedQuizQuestions)
  }

  // Helper function to check for failure
  private fun checkFailure(onFailure: (Exception) -> Unit): Boolean {
    if (shouldFail) {
      onFailure(failureException)
      return false
    }
    return true
  }
}
