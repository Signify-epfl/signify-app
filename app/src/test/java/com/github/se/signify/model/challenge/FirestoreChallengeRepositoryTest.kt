package com.github.se.signify.model.challenge

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.WriteBatch
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class FirestoreChallengeRepositoryTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore

  @Mock private lateinit var mockChallengesCollection: CollectionReference

  @Mock private lateinit var mockChallengeDocRef: DocumentReference

  @Mock private lateinit var mockBatch: WriteBatch

  private lateinit var firestoreChallengeRepository: FirestoreChallengeRepository

  private val challengeId = "challengeId"

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    `when`(mockFirestore.collection(any())).thenReturn(mockChallengesCollection)
    `when`(mockChallengesCollection.document(eq(challengeId))).thenReturn(mockChallengeDocRef)
    `when`(mockFirestore.batch()).thenReturn(mockBatch)

    `when`(mockBatch.set(any<DocumentReference>(), any<Challenge>(), any<SetOptions>()))
        .thenReturn(mockBatch)

    val mockPlayer1Collection = Mockito.mock(CollectionReference::class.java)
    val mockPlayer2Collection = Mockito.mock(CollectionReference::class.java)
    val mockPlayer1DocRef = Mockito.mock(DocumentReference::class.java)
    val mockPlayer2DocRef = Mockito.mock(DocumentReference::class.java)

    `when`(mockFirestore.collection("users")).thenReturn(mockChallengesCollection)
    `when`(mockChallengesCollection.document(eq("player1"))).thenReturn(mockPlayer1DocRef)
    `when`(mockChallengesCollection.document(eq("player2"))).thenReturn(mockPlayer2DocRef)
    `when`(mockPlayer1DocRef.collection("challenges")).thenReturn(mockPlayer1Collection)
    `when`(mockPlayer2DocRef.collection("challenges")).thenReturn(mockPlayer2Collection)
    `when`(mockPlayer1Collection.document(eq(challengeId))).thenReturn(mockPlayer1DocRef)
    `when`(mockPlayer2Collection.document(eq(challengeId))).thenReturn(mockPlayer2DocRef)

    `when`(mockBatch.set(eq(mockPlayer1DocRef), any<Challenge>(), any<SetOptions>()))
        .thenReturn(mockBatch)
    `when`(mockBatch.set(eq(mockPlayer2DocRef), any<Challenge>(), any<SetOptions>()))
        .thenReturn(mockBatch)

    firestoreChallengeRepository = FirestoreChallengeRepository(mockFirestore)
  }

  @Test
  fun deleteChallenge_shouldDeleteChallengeFromFirestore() {
    `when`(mockChallengeDocRef.delete()).thenReturn(Tasks.forResult(null))

    var successCalled = false
    firestoreChallengeRepository.deleteChallenge(
        challengeId = challengeId,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(successCalled)
    verify(mockChallengeDocRef).delete()
  }

  @Test
  fun deleteChallenge_shouldCallOnFailureOnFirestoreError() {
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.delete()).thenReturn(Tasks.forException(exception))

    var failureCalled = false
    firestoreChallengeRepository.deleteChallenge(
        challengeId = challengeId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockChallengeDocRef).delete()
  }

  @Test
  fun sendChallengeRequest_shouldCallOnFailureOnFirestoreError() {
    val exception = Exception("Firestore error")
    val mockTask = Tasks.forException<Void>(exception)

    `when`(mockBatch.set(any(), any<Challenge>(), any<SetOptions>())).thenReturn(mockBatch)
    `when`(mockBatch.commit()).thenReturn(mockTask)

    var failureCalled = false
    firestoreChallengeRepository.sendChallengeRequest(
        player1Id = "player1",
        player2Id = "player2",
        mode = ChallengeMode.CHRONO,
        challengeId = challengeId,
        roundWords = listOf("A", "B", "C"),
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockBatch).commit()
  }

  @Test
  fun getChallengeById_shouldReturnChallengeOnSuccess() {
    val mockDocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    val challenge = Challenge(challengeId = challengeId)
    `when`(mockDocumentSnapshot.toObject(Challenge::class.java)).thenReturn(challenge)

    `when`(mockChallengeDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    var successChallenge: Challenge? = null
    firestoreChallengeRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { successChallenge = it },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertEquals(challenge, successChallenge)
    verify(mockChallengeDocRef).get()
  }

  @Test
  fun getChallengeById_shouldCallOnFailureOnFirestoreError() {
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.get()).thenReturn(Tasks.forException(exception))

    var failureCalled = false
    firestoreChallengeRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockChallengeDocRef).get()
  }

  @Test
  fun getChallenges_shouldReturnChallengesOnSuccess() {
    val mockDocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    val challenge = Challenge(challengeId = challengeId)
    `when`(mockDocumentSnapshot.toObject(Challenge::class.java)).thenReturn(challenge)

    val mockChallengeDocRef = Mockito.mock(DocumentReference::class.java)
    `when`(mockChallengeDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))
    `when`(mockChallengesCollection.document(eq(challengeId))).thenReturn(mockChallengeDocRef)

    var successChallenges: List<Challenge>? = null
    firestoreChallengeRepository.getChallenges(
        challengeIds = listOf(challengeId),
        onSuccess = { successChallenges = it },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertEquals(listOf(challenge), successChallenges)
    verify(mockChallengeDocRef).get()
  }

  @Test
  fun getChallenges_returnsEmptyListWhenChallengeIdsAreInvalid() {
    val mockDocumentSnapshot = Mockito.mock(DocumentSnapshot::class.java)
    `when`(mockDocumentSnapshot.toObject(Challenge::class.java)).thenReturn(null)

    val mockChallengeDocRef = Mockito.mock(DocumentReference::class.java)
    `when`(mockChallengeDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))
    `when`(mockChallengesCollection.document(eq(challengeId))).thenReturn(mockChallengeDocRef)

    var successChallenges: List<Challenge>? = null
    firestoreChallengeRepository.getChallenges(
        challengeIds = listOf(challengeId),
        onSuccess = { successChallenges = it },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertEquals(emptyList<Challenge>(), successChallenges)
    verify(mockChallengeDocRef).get()
  }

  @Test
  fun updateChallenge_shouldUpdateChallengeInFirestore() {
    val updatedChallenge = Challenge(challengeId = challengeId)
    `when`(mockChallengeDocRef.set(updatedChallenge)).thenReturn(Tasks.forResult(null))

    var successCalled = false
    firestoreChallengeRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(successCalled)
    verify(mockChallengeDocRef).set(updatedChallenge)
  }

  @Test
  fun updateChallenge_shouldCallOnFailureOnFirestoreError() {
    val updatedChallenge = Challenge(challengeId = challengeId)
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.set(updatedChallenge)).thenReturn(Tasks.forException(exception))

    var failureCalled = false
    firestoreChallengeRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockChallengeDocRef).set(updatedChallenge)
  }

  @Test
  fun recordPlayerTime_shouldUpdatePlayerTimeInFirestore() {
    val playerId = "player1"
    val timeTaken: Long = 120
    `when`(mockChallengeDocRef.update("playerTime", timeTaken)).thenReturn(Tasks.forResult(null))

    var successCalled = false
    firestoreChallengeRepository.recordPlayerTime(
        challengeId = challengeId,
        playerId = playerId,
        timeTaken = timeTaken,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(successCalled)
    verify(mockChallengeDocRef).update("playerTime", timeTaken)
  }

  @Test
  fun recordPlayerTime_shouldCallOnFailureOnFirestoreError() {
    val playerId = "player1"
    val timeTaken: Long = 120
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.update("playerTime", timeTaken))
        .thenReturn(Tasks.forException(exception))

    var failureCalled = false
    firestoreChallengeRepository.recordPlayerTime(
        challengeId = challengeId,
        playerId = playerId,
        timeTaken = timeTaken,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockChallengeDocRef).update("playerTime", timeTaken)
  }

  @Test
  fun updateWinner_shouldUpdateWinnerInFirestore() {
    val winnerId = "winnerId"
    `when`(mockChallengeDocRef.update("winner", winnerId)).thenReturn(Tasks.forResult(null))

    var successCalled = false
    firestoreChallengeRepository.updateWinner(
        challengeId = challengeId,
        winnerId = winnerId,
        onSuccess = { successCalled = true },
        onFailure = { fail("Failure callback should not be called") })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(successCalled)
    verify(mockChallengeDocRef).update("winner", winnerId)
  }

  @Test
  fun updateWinner_shouldCallOnFailureOnFirestoreError() {
    val winnerId = "winnerId"
    val exception = Exception("Firestore error")
    `when`(mockChallengeDocRef.update("winner", winnerId)).thenReturn(Tasks.forException(exception))

    var failureCalled = false
    firestoreChallengeRepository.updateWinner(
        challengeId = challengeId,
        winnerId = winnerId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = {
          failureCalled = true
          assertEquals(exception, it)
        })

    shadowOf(Looper.getMainLooper()).idle()

    assertTrue(failureCalled)
    verify(mockChallengeDocRef).update("winner", winnerId)
  }
}
