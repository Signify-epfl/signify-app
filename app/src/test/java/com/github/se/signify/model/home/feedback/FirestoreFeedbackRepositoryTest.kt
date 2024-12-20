package com.github.se.signify.model.home.feedback

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class FirestoreFeedbackRepositoryTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockFeedbackCollection: CollectionReference
  @Mock private lateinit var mockFeedbackDocRef: DocumentReference

  private lateinit var firestoreFeedbackRepository: FirestoreFeedbackRepository

  private val feedback =
      Feedback(
          uid = "user123",
          type = "Bug Report",
          title = "Issue with Login",
          description = "I can't login using my Google account.",
          rating = 4)

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    // Set up the mocks for Firestore components
    `when`(mockFirestore.collection("feedback")).thenReturn(mockFeedbackCollection)
    `when`(mockFeedbackCollection.document(eq(feedback.title))).thenReturn(mockFeedbackDocRef)

    firestoreFeedbackRepository = FirestoreFeedbackRepository(mockFirestore)
  }

  @Test
  fun saveFeedback_shouldSaveFeedbackToFirestore() {
    // Arrange
    val feedbackMap =
        hashMapOf(
            "uid" to feedback.uid,
            "type" to feedback.type,
            "title" to feedback.title,
            "description" to feedback.description,
            "rating" to feedback.rating)

    // Mocking the set method to simulate a successful operation
    `when`(mockFeedbackDocRef.set(eq(feedbackMap), any<SetOptions>()))
        .thenReturn(Tasks.forResult(null))

    // Act
    var successCalled = false
    firestoreFeedbackRepository.saveFeedback(
        feedback.uid,
        feedback.type,
        feedback.title,
        feedback.description,
        feedback.rating,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue("The success callback should be called.", successCalled)
    verify(mockFeedbackDocRef).set(eq(feedbackMap), any<SetOptions>())
  }

  @Test
  fun saveFeedback_shouldCallOnErrorOnFirestoreError() {
    // Arrange
    val exception = Exception("Firestore error")
    `when`(mockFeedbackDocRef.set(any(), any<SetOptions>()))
        .thenReturn(Tasks.forException(exception))

    // Act
    var failureCalled = false
    firestoreFeedbackRepository.saveFeedback(
        feedback.uid,
        feedback.type,
        feedback.title,
        feedback.description,
        feedback.rating,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals("Firestore error", it.message)
        })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue("The failure callback should be called.", failureCalled)
    verify(mockFeedbackDocRef).set(any(), any<SetOptions>())
  }
}
