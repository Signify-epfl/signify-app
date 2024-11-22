package com.github.se.signify.model.feedback

import com.github.se.signify.model.auth.UserSession
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class FeedbackViewModelTest {

  private lateinit var userSession: UserSession
  private lateinit var feedbackRepository: FeedbackRepository
  private lateinit var feedbackViewModel: FeedbackViewModel

  private val feedback =
      Feedback(
          uid = "user123",
          type = "Bug Report",
          title = "Issue with Login",
          description = "I can't login using my Google account.",
          rating = 4)

  @Before
  fun setUp() {
    feedbackRepository = mock(FeedbackRepository::class.java)
    feedbackViewModel = FeedbackViewModel(userSession, feedbackRepository)
  }

  @Test
  fun saveFeedback_callsRepository() {
    // Act
    feedbackViewModel.saveFeedback(
        feedback.type, feedback.title, feedback.description, feedback.rating)

    // Assert: Verify that the repository's saveFeedback method was called
    verify(feedbackRepository)
        .saveFeedback(
            eq(userSession.getUserId()!!),
            eq(feedback.type),
            eq(feedback.title),
            eq(feedback.description),
            eq(feedback.rating),
            any(),
            any())
  }
}
