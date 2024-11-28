package com.github.se.signify.model.challenge

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.*
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

  @Mock private lateinit var mockBatch: WriteBatch

  private lateinit var challengeRepositoryFireStore: ChallengeRepositoryFireStore

  private val challengeId = "challengeId"
  // private val player1Id = "player1Id"
  // private val player2Id = "player2Id"
  // private val mode = ChallengeMode.CHRONO

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
    `when`(mockFirestore.batch()).thenReturn(mockBatch)
    `when`(mockBatch.set(any(), any<Any>(), any<SetOptions>())).thenReturn(mockBatch)

    challengeRepositoryFireStore = ChallengeRepositoryFireStore(mockFirestore)
  }

  /**
   * @Test fun sendChallengeRequest_shouldCreateChallengeInFirestore() { // Arrange
   *   `when`(mockBatch.commit()).thenReturn(Tasks.forResult(null))
   *
   * // Act var successCalled = false challengeRepositoryFireStore.sendChallengeRequest( player1Id =
   * player1Id, player2Id = player2Id, mode = mode, challengeId = challengeId, onSuccess = {
   * successCalled = true }, onFailure = { fail("Failure callback should not be called") } )
   *
   * // Idle the main looper to process the tasks shadowOf(Looper.getMainLooper()).idle()
   *
   * // Assert assertTrue(successCalled) verify(mockBatch).commit() }
   *
   * @Test fun sendChallengeRequest_shouldCallOnFailureOnFirestoreError() { // Arrange val exception
   *   = Exception("Firestore error")
   *   `when`(mockBatch.commit()).thenReturn(Tasks.forException(exception))
   *
   * // Act var failureCalled = false challengeRepositoryFireStore.sendChallengeRequest( player1Id =
   * player1Id, player2Id = player2Id, mode = mode, challengeId = challengeId, onSuccess = {
   * fail("Success callback should not be called") }, onFailure = { failureCalled = true
   * assertEquals(exception, it) } )
   *
   * // Idle the main looper to process the tasks shadowOf(Looper.getMainLooper()).idle()
   *
   * // Assert assertTrue(failureCalled) verify(mockBatch).commit() }
   */
  @Test
  fun deleteChallenge_shouldDeleteChallengeFromFirestore() {
    // Arrange
    `when`(mockChallengeDocRef.delete()).thenReturn(Tasks.forResult(null))

    // Act
    var successCalled = false
    challengeRepositoryFireStore.deleteChallenge(
        challengeId = challengeId,
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
        challengeId = challengeId,
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
/**
 * @Test fun updateChallenge_shouldUpdateChallengeInFirestore() { // Arrange val challenge =
 *   Challenge(challengeId = challengeId, player1 = player1Id, player2 = player2Id)
 *   `when`(mockChallengeDocRef.set(eq(challenge),
 *   any<SetOptions>())).thenReturn(Tasks.forResult(null))
 *
 * // Act var successCalled = false challengeRepositoryFireStore.updateChallenge( updatedChallenge =
 * challenge, onSuccess = { successCalled = true }, onFailure = { fail("Failure callback should not
 * be called") } )
 *
 * // Idle the main looper to process the tasks shadowOf(Looper.getMainLooper()).idle()
 *
 * // Assert assertTrue(successCalled) verify(mockChallengeDocRef).set(eq(challenge),
 * any<SetOptions>()) }
 *
 * @Test fun updateChallenge_shouldCallOnFailureOnFirestoreError() { // Arrange val challenge =
 *   Challenge(challengeId = challengeId, player1 = player1Id, player2 = player2Id) val exception =
 *   Exception("Firestore error") `when`(mockChallengeDocRef.set(eq(challenge),
 *   any<SetOptions>())).thenReturn(Tasks.forException(exception))
 *
 * // Act var failureCalled = false challengeRepositoryFireStore.updateChallenge( updatedChallenge =
 * challenge, onSuccess = { fail("Success callback should not be called") }, onFailure = {
 * failureCalled = true assertEquals(exception, it) } )
 *
 * // Idle the main looper to process the tasks shadowOf(Looper.getMainLooper()).idle()
 *
 * // Assert assertTrue(failureCalled) verify(mockChallengeDocRef).set(eq(challenge),
 * any<SetOptions>()) } }
 */
