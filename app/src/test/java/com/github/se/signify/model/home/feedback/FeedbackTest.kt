package com.github.se.signify.model.home.feedback

import com.github.se.signify.model.home.feedback.Feedback
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class FeedbackTest {

  @Test
  fun `default constructor initializes properties correctly`() {
    val feedback = Feedback()

    assertEquals("", feedback.uid)
    assertEquals("", feedback.type)
    assertEquals("", feedback.title)
    assertEquals("", feedback.description)
    assertEquals(0, feedback.rating)
  }

  @Test
  fun `parameterized constructor initializes properties correctly`() {
    val feedback =
        Feedback(
            uid = "user123",
            type = "Bug Report",
            title = "Issue with Login",
            description = "I can't login using my Google account.",
            rating = 4)

    assertEquals("user123", feedback.uid)
    assertEquals("Bug Report", feedback.type)
    assertEquals("Issue with Login", feedback.title)
    assertEquals("I can't login using my Google account.", feedback.description)
    assertEquals(4, feedback.rating)
  }

  @Test
  fun `equality check between two identical Feedback objects`() {
    val feedback1 =
        Feedback(
            uid = "user123",
            type = "Bug Report",
            title = "Issue with Login",
            description = "I can't login using my Google account.",
            rating = 4)
    val feedback2 =
        Feedback(
            uid = "user123",
            type = "Bug Report",
            title = "Issue with Login",
            description = "I can't login using my Google account.",
            rating = 4)

    assertEquals(feedback1, feedback2)
    assertEquals(feedback1.hashCode(), feedback2.hashCode())
  }

  @Test
  fun `inequality check between different Feedback objects`() {
    val feedback1 = Feedback(title = "Issue with Login")
    val feedback2 = Feedback(title = "Feature Request")

    assertNotEquals(feedback1, feedback2)
  }

  @Test
  fun `copy function creates a new Feedback with modified properties`() {
    val feedback =
        Feedback(
            uid = "user123",
            type = "Bug Report",
            title = "Issue with Login",
            description = "I can't login using my Google account.",
            rating = 4)

    val modifiedFeedback = feedback.copy(description = "Updated description", rating = 5)

    assertEquals("user123", modifiedFeedback.uid)
    assertEquals("Bug Report", modifiedFeedback.type)
    assertEquals("Issue with Login", modifiedFeedback.title)
    assertEquals("Updated description", modifiedFeedback.description) // Modified
    assertEquals(5, modifiedFeedback.rating) // Modified
  }
}
