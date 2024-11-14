package com.github.se.signify.model.user

import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.challenge.Challenge
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.time.LocalDate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class UserRepositoryFireStoreTest {

  @Mock private lateinit var mockFireStore: FirebaseFirestore
  @Mock private lateinit var mockChallengeDocRef: DocumentReference // Mock for challenge
  @Mock private lateinit var mockCurrentUserDocRef: DocumentReference // Mock for current user
  @Mock private lateinit var mockFriendUserDocRef: DocumentReference // Mock for friend user
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockUserDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockToDoQuerySnapshot: QuerySnapshot

  private lateinit var userRepositoryFireStore: UserRepositoryFireStore

  // Arrange
  private val currentUserId = "currentUserId"
  private val friendUserId = "friendUserId"
  private val challengeId1 = "challengeId"
  private val challengeId2 = "challengeId2"

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    userRepositoryFireStore = UserRepositoryFireStore(mockFireStore)

    `when`(mockFireStore.collection(any())).thenReturn(mockCollectionReference)

    `when`(mockCollectionReference.document(currentUserId)).thenReturn(mockCurrentUserDocRef)
    `when`(mockCollectionReference.document(friendUserId)).thenReturn(mockFriendUserDocRef)
    `when`(mockFireStore.collection("challenges").document(challengeId1))
        .thenReturn(mockChallengeDocRef)
    `when`(mockFireStore.collection("challenges").document(challengeId2))
        .thenReturn(mockChallengeDocRef)

    // Mock the update method to simulate success
    `when`(mockCurrentUserDocRef.update(anyString(), any())).thenReturn(Tasks.forResult(null))
    `when`(mockFriendUserDocRef.update(anyString(), any())).thenReturn(Tasks.forResult(null))
  }

  @Test
  fun getFriendsList_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    userRepositoryFireStore.getFriendsList(
        currentUserId,
        onSuccess = {
          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  @Test
  fun getRequestsFriendsList_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    userRepositoryFireStore.getRequestsFriendsList(
        currentUserId,
        onSuccess = {
          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  @Test
  fun getUserName_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    userRepositoryFireStore.getUserName(
        currentUserId,
        onSuccess = {
          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  @Test
  fun getProfilePictureUrl_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    userRepositoryFireStore.getProfilePictureUrl(
        currentUserId,
        onSuccess = {
          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  @Test
  fun updateUserName_shouldUpdateFireStoreDocument() {
    // Mock the FireStore document reference and its update method
    `when`(mockCurrentUserDocRef.update(eq("name"), eq("NewName")))
        .thenReturn(Tasks.forResult(null)) // Simulate success

    var successCallbackCalled = false
    val onSuccess: () -> Unit = { successCallbackCalled = true }

    // Act
    userRepositoryFireStore.updateUserName(
        currentUserId,
        "NewName",
        onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCallbackCalled)

    // Verify that FireStore's update method was called with the correct arguments
    verify(mockCurrentUserDocRef).update(eq("name"), eq("NewName"))
  }

  @Test
  fun updateUserName_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure") // Simulate FireStore failure

    // Mock FireStore document reference to fail the update
    `when`(mockCurrentUserDocRef.update(eq("name"), eq("NewName")))
        .thenReturn(Tasks.forException(testException)) // Simulate failure

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure the correct exception is passed
    }

    // Act
    userRepositoryFireStore.updateUserName(
        currentUserId,
        "NewName",
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled)

    // Verify that FireStore's update method was called with the correct arguments
    verify(mockCurrentUserDocRef).update(eq("name"), eq("NewName"))
  }

  @Test
  fun updateProfilePictureUrl_shouldUpdateFireStoreDocument() {
    // Mock the FireStore document reference and its update method
    `when`(mockCurrentUserDocRef.update(eq("profileImageUrl"), eq("NewProfilePictureUrl")))
        .thenReturn(Tasks.forResult(null)) // Simulate success

    var successCallbackCalled = false
    val onSuccess: () -> Unit = { successCallbackCalled = true }

    // Act
    userRepositoryFireStore.updateProfilePictureUrl(
        currentUserId,
        "NewProfilePictureUrl",
        onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCallbackCalled)

    // Verify that FireStore's update method was called with the correct arguments
    verify(mockCurrentUserDocRef).update(eq("profileImageUrl"), eq("NewProfilePictureUrl"))
  }

  @Test
  fun updateProfilePictureUrl_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test FireStore failure") // Simulate Firestore failure

    // Mock FireStore document reference to fail the update
    `when`(mockCurrentUserDocRef.update(eq("profileImageUrl"), eq("NewProfilePictureUrl")))
        .thenReturn(Tasks.forException(testException)) // Simulate failure

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure the correct exception is passed
    }

    // Act
    userRepositoryFireStore.updateProfilePictureUrl(
        currentUserId,
        "NewProfilePictureUrl",
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled)

    // Verify that FireStore's update method was called with the correct arguments
    verify(mockCurrentUserDocRef).update(eq("profileImageUrl"), eq("NewProfilePictureUrl"))
  }

  @Test
  fun getUserById_shouldCallOnSuccessWhenUserExists() {
    // Arrange
    val mockUser =
        User(
            uid = "testUserId",
            name = "Test User",
            email = "testuser@example.com",
            profileImageUrl = null,
            friendRequests = listOf("fr1", "fr2"),
            friends = listOf("f1", "f2"),
            currentStreak = 0,
            highestStreak = 0)
    val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)

    // Simulate that the document exists
    `when`(mockDocumentSnapshot.exists()).thenReturn(true)

    // Simulate successful conversion of document to User object
    `when`(mockDocumentSnapshot.toObject(User::class.java)).thenReturn(mockUser)

    // Simulate FireStore get() method returning the document snapshot
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    var successCallbackCalled = false
    val onSuccess: (User) -> Unit = { user ->
      successCallbackCalled = true
      assertEquals(mockUser, user) // Ensure the correct user is passed
    }

    // Act
    userRepositoryFireStore.getUserById(
        currentUserId,
        onSuccess = onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCallbackCalled)

    // Verify that the FireStore `get()` method was called
    verify(mockCurrentUserDocRef).get()
  }

  @Test
  fun getUserById_shouldCallOnFailureWhenUserNotFound() {
    // Arrange
    val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)

    // Simulate that the document does not exist
    `when`(mockDocumentSnapshot.exists()).thenReturn(false)

    // Simulate FireStore get() method returning a successful result, but with a non-existent
    // document
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals("User not found", exception.message)
    }

    // Act
    userRepositoryFireStore.getUserById(
        currentUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled)

    // Verify that the FireStore `get()` method was called
    verify(mockCurrentUserDocRef).get()
  }

  @Test
  fun getUserById_shouldCallOnFailureWhenFireStoreFails() {
    // Arrange
    val testException = Exception("Test FireStore failure") // Simulate FireStore failure

    // Simulate FireStore failing to retrieve the document
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forException(testException))

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception)
    }

    // Act
    userRepositoryFireStore.getUserById(
        currentUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled)

    // Verify that the FireStore `get()` method was called
    verify(mockCurrentUserDocRef).get()
  }

  @Test
  fun sendFriendRequest_shouldUpdateFireStoreDocuments() {

    // Mock the FireStore document references and their update methods
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates

    // Act
    userRepositoryFireStore.sendFriendRequest(
        currentUserId, friendUserId, onSuccess = {}, onFailure = {})

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockFriendUserDocRef)
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun sendFriendRequest_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure") // Simulate Firestore failure

    // Mock FireStore document reference to fail the update
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException)) // Simulate failure for update

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure the correct exception is passed
    }

    // Act
    userRepositoryFireStore.sendFriendRequest(
        currentUserId,
        friendUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert that the failure callback was triggered
    assertTrue(failureCallbackCalled)

    // Verify that FireStore update was attempted
    verify(mockFriendUserDocRef)
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun acceptFriendRequest_shouldUpdateFireStoreDocuments() {

    // Mock the FireStore document references and their update methods
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates

    // Act
    userRepositoryFireStore.acceptFriendRequest(
        currentUserId, friendUserId, onSuccess = {}, onFailure = {})

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockCurrentUserDocRef)
        .update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    verify(mockFriendUserDocRef).update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    verify(mockCurrentUserDocRef)
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun acceptFriendRequest_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure") // Simulate Firestore failure

    // Mock FireStore document references with one of the updates failing
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for the first update

    // Simulate failure for the second update
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException)) // Simulate failure

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure correct exception is passed
    }

    // Act
    userRepositoryFireStore.acceptFriendRequest(
        currentUserId,
        friendUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled) // Ensure the failure callback was invoked

    // Verify that the first update (to currentUserRef) was attempted and failed
    verify(mockCurrentUserDocRef)
        .update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    // Since currentUserRef update failed, friendUserRef update should not be called
    verify(mockFriendUserDocRef, never())
        .update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    // The update to `friendRequests` should also not have been called due to the failure
    verify(mockCurrentUserDocRef, never())
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun declineFriendRequest_shouldUpdateFireStoreDocuments() {

    // Mock the FireStore document references and their update methods
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates

    // Act
    userRepositoryFireStore.declineFriendRequest(
        currentUserId, friendUserId, onSuccess = {}, onFailure = {})

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockCurrentUserDocRef)
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun declineFriendRequest_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure") // Simulate Firestore failure

    // Mock FireStore document reference with failure for the update call
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException)) // Simulate failure for update

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure correct exception is passed
    }

    // Act
    userRepositoryFireStore.declineFriendRequest(
        currentUserId,
        friendUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert that the failure callback was triggered
    assertTrue(failureCallbackCalled)

    // Verify that FireStore `update()` was attempted and failed
    verify(mockCurrentUserDocRef)
        .update(eq("friendRequests"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun removeFriend_shouldUpdateFireStoreDocuments() {

    // Mock the FireStore document references and their update methods
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for updates

    // Act
    userRepositoryFireStore.removeFriend(
        currentUserId, friendUserId, onSuccess = {}, onFailure = {})

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockCurrentUserDocRef)
        .update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    verify(mockFriendUserDocRef).update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun removeFriend_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure") // Simulate Firestore failure

    // Mock FireStore document references with one of the updates failing
    `when`(mockFriendUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException)) // Simulate failure for friendUser update

    // Simulate success for currentUser update
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forResult(null)) // Simulate success for currentUser update

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure correct exception is passed
    }

    // Act
    userRepositoryFireStore.removeFriend(
        currentUserId,
        friendUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert that the failure callback was triggered
    assertTrue(failureCallbackCalled)

    // Verify that the currentUser update was called before the failure
    verify(mockCurrentUserDocRef)
        .update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))

    // Verify that the friendUser update was attempted and failed
    verify(mockFriendUserDocRef).update(eq("friends"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun addOngoingChallenge_shouldUpdateFireStoreDocuments() {
    // Act
    userRepositoryFireStore.addOngoingChallenge(
        currentUserId,
        challengeId1,
        onSuccess = {},
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockCurrentUserDocRef)
        .update(eq("ongoingChallenges"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun addOngoingChallenge_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure")

    // Mock FireStore document reference to fail the update
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException))

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception)
    }

    // Act
    userRepositoryFireStore.addOngoingChallenge(
        currentUserId,
        challengeId1,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert that the failure callback was triggered
    assertTrue(failureCallbackCalled)
  }

  @Test
  fun removeOngoingChallenge_shouldUpdateFireStoreDocuments() {
    // Act
    userRepositoryFireStore.removeOngoingChallenge(
        currentUserId,
        challengeId1,
        onSuccess = {},
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Verify that FireStore update methods were called with the correct arguments
    verify(mockCurrentUserDocRef)
        .update(eq("ongoingChallenges"), ArgumentMatchers.any(FieldValue::class.java))
  }

  @Test
  fun removeOngoingChallenge_shouldCallOnFailureWhenUpdateFails() {
    // Arrange
    val testException = Exception("Test Firestore failure")

    // Mock FireStore document reference to fail the update
    `when`(mockCurrentUserDocRef.update(anyString(), any()))
        .thenReturn(Tasks.forException(testException))

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception)
    }

    // Act
    userRepositoryFireStore.removeOngoingChallenge(
        currentUserId,
        challengeId1,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert that the failure callback was triggered
    assertTrue(failureCallbackCalled)
  }

  @Test
  fun getOngoingChallenges_shouldReturnEmptyListWhenNoChallenges() {
    // Arrange
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockUserDocumentSnapshot))
    `when`(mockUserDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockUserDocumentSnapshot.get("ongoingChallenges")).thenReturn(null)

    var successCallbackCalled = false
    val onSuccess: (List<Challenge>) -> Unit = { challenges ->
      successCallbackCalled = true
      assertTrue(challenges.isEmpty()) // Ensure the challenges list is empty
    }

    // Act
    userRepositoryFireStore.getOngoingChallenges(
        currentUserId, onSuccess, onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(successCallbackCalled)
  }

  @Test
  fun getOngoingChallenges_shouldCallOnFailureWhenChallengeFetchFails() {
    // Arrange
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockUserDocumentSnapshot))
    `when`(mockUserDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockUserDocumentSnapshot.get("ongoingChallenges")).thenReturn(listOf(challengeId1))

    // Mock challenge document to simulate failure
    val testException = Exception("Test Firestore failure")
    `when`(mockChallengeDocRef.get()).thenReturn(Tasks.forException(testException))

    var failureCallbackCalled = false
    val onFailure: (Exception) -> Unit = { exception ->
      failureCallbackCalled = true
      assertEquals(testException, exception) // Ensure the correct exception is passed
    }

    // Act
    userRepositoryFireStore.getOngoingChallenges(
        currentUserId,
        onSuccess = { fail("Success callback should not be called") },
        onFailure = onFailure)

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    assertTrue(failureCallbackCalled)
  }

  @Test
  fun getStreak_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockToDoQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockToDoQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    userRepositoryFireStore.getStreak(
        currentUserId,
        onSuccess = {
          // Do nothing; we just want to verify that the 'documents' field was accessed
        },
        onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockToDoQuerySnapshot).documents }
  }

  @Test
  fun updateStreak_shouldUpdateStreakCorrectly() {
    // Arrange: Mock existing user data
    val lastLoginDate = "2024-11-10"
    val currentStreak = 0L
    val updatedDataCaptor = argumentCaptor<Map<String, Any>>()

    `when`(mockUserDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockUserDocumentSnapshot.getString("lastLoginDate")).thenReturn(lastLoginDate)
    `when`(mockUserDocumentSnapshot.getLong("currentStreak")).thenReturn(currentStreak)
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockUserDocumentSnapshot))
    `when`(mockCurrentUserDocRef.update(updatedDataCaptor.capture()))
        .thenReturn(Tasks.forResult(null))

    var successCallbackCalled = false
    val onSuccess: () -> Unit = { successCallbackCalled = true }

    // Act: Call the method under test
    userRepositoryFireStore.updateStreak(
        currentUserId,
        onSuccess = onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert: Verify correct data updates
    assertTrue(successCallbackCalled)
    val capturedData = updatedDataCaptor.firstValue

    // Verify lastLoginDate is updated
    assertEquals(LocalDate.now().toString(), capturedData["lastLoginDate"])

    // Verify streak update logic
    val expectedCurrentStreak = currentStreak + 1L
    assertEquals(expectedCurrentStreak.toInt(), capturedData["currentStreak"])

    // Verify FireStore update is called
    verify(mockCurrentUserDocRef).update(updatedDataCaptor.capture())
  }

  @Test
  fun updateStreak_shouldResetStreakOnGap() {
    // Arrange: Mock data with a gap in login dates
    val lastLoginDate = "2024-11-01"
    val currentStreak = 3L
    val highestStreak = 5L
    val updatedDataCaptor = argumentCaptor<Map<String, Any>>()

    `when`(mockUserDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockUserDocumentSnapshot.getString("lastLoginDate")).thenReturn(lastLoginDate)
    `when`(mockUserDocumentSnapshot.getLong("currentStreak")).thenReturn(currentStreak)
    `when`(mockUserDocumentSnapshot.getLong("highestStreak")).thenReturn(highestStreak)
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockUserDocumentSnapshot))
    `when`(mockCurrentUserDocRef.update(updatedDataCaptor.capture()))
        .thenReturn(Tasks.forResult(null))

    var successCallbackCalled = false
    val onSuccess: () -> Unit = { successCallbackCalled = true }

    // Act: Call the method under test
    userRepositoryFireStore.updateStreak(
        currentUserId,
        onSuccess = onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert: Verify correct data updates
    assertTrue(successCallbackCalled)
    val capturedData = updatedDataCaptor.firstValue

    // Verify lastLoginDate is updated
    assertEquals(LocalDate.now().toString(), capturedData["lastLoginDate"])

    // Verify streak reset logic
    assertEquals(1, capturedData["currentStreak"])

    // Verify FireStore update is called
    verify(mockCurrentUserDocRef).update(updatedDataCaptor.capture())
  }

  @Test
  fun updateStreak_shouldHandleFirstLogin() {
    // Arrange: Mock data with no previous login
    `when`(mockUserDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockUserDocumentSnapshot.getString("lastLoginDate")).thenReturn(null)
    `when`(mockUserDocumentSnapshot.getLong("currentStreak")).thenReturn(null)
    `when`(mockUserDocumentSnapshot.getLong("highestStreak")).thenReturn(null)
    `when`(mockCurrentUserDocRef.get()).thenReturn(Tasks.forResult(mockUserDocumentSnapshot))
    `when`(mockCurrentUserDocRef.update(any<Map<String, Any>>())).thenReturn(Tasks.forResult(null))

    var successCallbackCalled = false
    val onSuccess: () -> Unit = { successCallbackCalled = true }

    // Act: Call the method under test
    userRepositoryFireStore.updateStreak(
        currentUserId,
        onSuccess = onSuccess,
        onFailure = { fail("Failure callback should not be called") })

    // Idle the main looper to process the tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert: Verify correct data updates
    assertTrue(successCallbackCalled)
    verify(mockCurrentUserDocRef)
        .update(
            mapOf(
                "lastLoginDate" to LocalDate.now().toString(),
                "currentStreak" to 1,
                "highestStreak" to 1L))
  }
}
