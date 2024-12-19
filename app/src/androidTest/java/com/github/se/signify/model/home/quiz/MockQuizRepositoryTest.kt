package com.github.se.signify.model.home.quiz

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class MockQuizRepositoryTest {

  private lateinit var mockQuizRepository: MockQuizRepository

  @Before
  fun setUp() {
    mockQuizRepository = MockQuizRepository()
    mockQuizRepository.succeed() // Default to succeed
  }

  @Test
  fun succeedWorks() {
    var successCount = 0
    val onSuccess: () -> Unit = { successCount += 1 }

    mockQuizRepository.init(onSuccess)

    mockQuizRepository.getQuizQuestions(
        onSuccess = { quizQuestions ->
          successCount += 1
          assertEquals(1, quizQuestions.size)
          assertEquals("apple", quizQuestions[0].correctWord)
          assertEquals(5, quizQuestions[0].signs.size)
          assertEquals(listOf("juice", "fruit", "grape"), quizQuestions[0].confusers)
        },
        onFailure = { fail("Should not fail") })

    assertEquals(2, successCount)
  }

  @Test
  fun failWorks() {
    var failureCount = 0
    val onFailure: (Exception) -> Unit = { failureCount += 1 }

    mockQuizRepository.fail()

    mockQuizRepository.getQuizQuestions(
        onSuccess = { fail("Should not succeed") }, onFailure = onFailure)

    assertEquals(1, failureCount) // One failure callback should have fired
  }

  @Test
  fun getQuizQuestionsReturnsCorrectData() {
    mockQuizRepository.getQuizQuestions(
        onSuccess = { quizQuestions ->
          assertEquals(1, quizQuestions.size)
          val question = quizQuestions[0]
          assertEquals("apple", question.correctWord)
          assertEquals(5, question.signs.size)
          assertEquals(listOf("juice", "fruit", "grape"), question.confusers)
        },
        onFailure = { fail("Should not fail") })
  }

  @Test
  fun initInvokesOnSuccessWhenNotFailing() {
    var initCalled = false
    val onSuccess: () -> Unit = { initCalled = true }

    mockQuizRepository.init(onSuccess)

    assertTrue("Expected init to succeed", initCalled)
  }

  @Test
  fun initDoesNotInvokeOnSuccessWhenFailing() {
    var initCalled = false
    val onSuccess: () -> Unit = { initCalled = true }

    mockQuizRepository.fail()
    mockQuizRepository.init(onSuccess)

    assertTrue("Expected init to fail", !initCalled)
  }
}
