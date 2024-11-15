package com.github.se.signify.model.challenge

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
class ChallengeRepositoryFireStoreTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockChallengesCollection: CollectionReference
  @Mock private lateinit var mockChallengeDocRef: DocumentReference

  private lateinit var challengeRepositoryFireStore: ChallengeRepositoryFireStore

  private val challengeId = "challengeId"
  private val player1Id = "player1Id"
  private val player2Id = "player2Id"
  private val mode = ChallengeMode.SPRINT

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    // Set up the mocks
    `when`(mockFirestore.collection(any())).thenReturn(mockChallengesCollection)
    `when`(mockChallengesCollection.document(eq(challengeId))).thenReturn(mockChallengeDocRef)

    challengeRepositoryFireStore = ChallengeRepositoryFireStore(mockFirestore)
  }

  @Test
  fun sendChallengeRequest_shouldCreateChallengeInFirestore() {
    // Arrange
    val challenge =
        hashMapOf(
            "challengeId" to challengeId,
            "player1" to player1Id,
            "player2" to player2Id,
            "status" to "pending",
            "round" to 1,
            "mode" to mode.name,
            "player1Score" to 0,
            "player2Score" to 0,
            "currentGesture" to "",
            "responses" to hashMapOf<String, String>())

    `when`(mockChallengeDocRef.set(eq(challenge), any<SetOptions>()))
        .thenReturn(Tasks.forResult(null))

    // Act
    var successCalled = false
    challengeRepositoryFireStore.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCalled)
    verify(mockChallengeDocRef).set(eq(challenge), any<SetOptions>())
  }

  @Test
  fun sendChallengeRequest_shouldCallOnFailureOnFirestoreError() {
    // Arrange
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.set(any(), any<SetOptions>()))
        .thenReturn(Tasks.forException(exception))

    // Act
    var failureCalled = false
    challengeRepositoryFireStore.sendChallengeRequest(
        player1Id,
        player2Id,
        mode,
        challengeId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCalled)
    verify(mockChallengeDocRef).set(any(), any<SetOptions>())
  }

  @Test
  fun deleteChallenge_shouldDeleteChallengeFromFirestore() {
    // Arrange
    `when`(mockChallengeDocRef.delete()).thenReturn(Tasks.forResult(null))

    // Act
    var successCalled = false
    challengeRepositoryFireStore.deleteChallenge(
        challengeId,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCalled)
    verify(mockChallengeDocRef).delete()
  }

  @Test
  fun deleteChallenge_shouldCallOnFailureOnFirestoreError() {
    // Arrange
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.delete()).thenReturn(Tasks.forException(exception))

    // Act
    var failureCalled = false
    challengeRepositoryFireStore.deleteChallenge(
        challengeId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCalled)
    verify(mockChallengeDocRef).delete()
  }
}
