package com.github.se.signify.model.quiz

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class QuizViewModelTest {

  private lateinit var quizRepository: QuizRepository
  private lateinit var quizViewModel: QuizViewModel

  @Before
  fun setUp() {
    quizRepository = mock(QuizRepository::class.java)
    quizViewModel = QuizViewModel(quizRepository)
  }

  @Test
  fun getQuizQuestionsCallsRepository() {
    // Act
    quizViewModel.getQuizQuestions()

    // Assert
    verify(quizRepository).getQuizQuestions(any(), any())
  }

  @Test
  fun getQuizQuestionsUpdatesQuizzesStateFlow() = runBlocking {
    // Arrange: Mock repository response
    val mockQuizzes =
        listOf(
            QuizQuestion("Apple", listOf(1, 2, 3), listOf("Banana", "Orange")),
            QuizQuestion("Car", listOf(4, 5, 6), listOf("Bike", "Bus")))
    doAnswer {
          val onSuccess = it.arguments[0] as (List<QuizQuestion>) -> Unit
          onSuccess(mockQuizzes)
        }
        .`when`(quizRepository)
        .getQuizQuestions(any(), any())

    // Act
    quizViewModel.getQuizQuestions()

    // Assert
    assert(quizViewModel.quizzes.first() == mockQuizzes)
  }

  @Test
  fun submitAnswerCallsOnCorrectForCorrectAnswer() {
    // Arrange
    val quiz = QuizQuestion("Apple", listOf(1, 2, 3), listOf("Banana", "Orange"))
    quizViewModel.quizzesTesting.value = listOf(quiz)
    quizViewModel.currentQuizTesting.value = quiz

    val mockQuizzes = listOf(quiz)
    doAnswer {
          val onSuccess = it.arguments[0] as (List<QuizQuestion>) -> Unit
          onSuccess(mockQuizzes)
        }
        .`when`(quizRepository)
        .getQuizQuestions(any(), any())

    var onCorrectCalled = false
    var onIncorrectCalled = false

    // Act
    quizViewModel.submitAnswer(
        selectedOption = "Apple",
        onCorrect = { onCorrectCalled = true },
        onIncorrect = { onIncorrectCalled = true })

    // Assert
    assert(onCorrectCalled)
    assert(!onIncorrectCalled)
  }

  @Test
  fun submitAnswerCallsOnIncorrectForWrongAnswer() {
    // Arrange
    val quiz = QuizQuestion("Apple", listOf(1, 2, 3), listOf("Banana", "Orange"))
    quizViewModel.quizzesTesting.value = listOf(quiz)
    quizViewModel.currentQuizTesting.value = quiz

    var onCorrectCalled = false
    var onIncorrectCalled = false

    // Act
    quizViewModel.submitAnswer(
        selectedOption = "Banana",
        onCorrect = { onCorrectCalled = true },
        onIncorrect = { onIncorrectCalled = true })

    // Assert
    assert(!onCorrectCalled)
    assert(onIncorrectCalled)
  }

  @Test
  fun submitAnswerSelectsNewRandomQuizAfterCorrectAnswer() {
    // Arrange
    val mockQuizzes =
        listOf(
            QuizQuestion("Apple", listOf(1, 2, 3), listOf("Banana", "Orange")),
            QuizQuestion("Car", listOf(4, 5, 6), listOf("Bike", "Bus")))
    quizViewModel.quizzesTesting.value = mockQuizzes
    quizViewModel.currentQuizTesting.value = mockQuizzes[0]

    var onCorrectCalled = false

    // Act
    quizViewModel.submitAnswer(
        selectedOption = "Apple", onCorrect = { onCorrectCalled = true }, onIncorrect = {})

    // Assert
    assert(onCorrectCalled)
    assert(quizViewModel.currentQuizTesting.value in mockQuizzes)
  }

  @Test
  fun getDailyQuestCallsRepository() {
    quizViewModel.getQuizQuestions()
    verify(quizRepository).getQuizQuestions(any(), any())
  }
}
