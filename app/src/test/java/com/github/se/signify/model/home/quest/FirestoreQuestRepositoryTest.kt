package com.github.se.signify.model.home.quest

import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FirestoreQuestRepositoryTest {
  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockDocumentReference: DocumentReference
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockQuestQuerySnapshot: QuerySnapshot
  @Mock private lateinit var mockTask: Task<QuerySnapshot>

  private lateinit var mockAuth: FirebaseAuth
  private lateinit var mockUser: FirebaseUser

  private lateinit var firestoreQuestRepository: FirestoreQuestRepository
  @Captor private lateinit var captor: ArgumentCaptor<OnCompleteListener<QuerySnapshot>>

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    firestoreQuestRepository = FirestoreQuestRepository(mockFirestore)

    `when`(mockFirestore.collection(any())).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(any())).thenReturn(mockDocumentReference)
    `when`(mockCollectionReference.document()).thenReturn(mockDocumentReference)

    mockDocumentSnapshot = mock(DocumentSnapshot::class.java)
    mockQuestQuerySnapshot = mock(QuerySnapshot::class.java)

    // Mock Firebase Auth instance
    mockAuth = mock(FirebaseAuth::class.java)
    mockUser = mock(FirebaseUser::class.java)
  }

  @Test
  fun init_triggersOnSuccessWhenUserIsLoggedIn() {
    // Mock the static Firebase.auth to return our mockAuth instance
    mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
      firebaseAuthMock.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(mockAuth)
      `when`(mockAuth.currentUser).thenReturn(mockUser)

      // Capture the AuthStateListener
      val authStateListenerCaptor = argumentCaptor<FirebaseAuth.AuthStateListener>()
      doNothing().`when`(mockAuth).addAuthStateListener(authStateListenerCaptor.capture())

      // Act & Assert
      var callbackTriggered = false
      firestoreQuestRepository.init {
        callbackTriggered = true // Set flag if onSuccess is triggered
      }

      // Simulate Firebase invoking the auth state listener
      authStateListenerCaptor.firstValue.onAuthStateChanged(mockAuth)

      // Assert
      assert(callbackTriggered) { "The onSuccess callback was not triggered." }
    }
  }

  @Test
  fun init_doesNotTriggerOnSuccessWhenUserIsNotLoggedIn() {
    // Mock the static Firebase.auth to return our mockAuth instance
    mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
      firebaseAuthMock.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(mockAuth)
      `when`(mockAuth.currentUser).thenReturn(null) // Simulate user not logged in

      // Capture the AuthStateListener
      val authStateListenerCaptor = argumentCaptor<FirebaseAuth.AuthStateListener>()
      doNothing().`when`(mockAuth).addAuthStateListener(authStateListenerCaptor.capture())

      // Act & Assert
      var callbackTriggered = false
      firestoreQuestRepository.init {
        callbackTriggered = true // Set flag if onSuccess is triggered
      }

      // Simulate Firebase invoking the auth state listener
      authStateListenerCaptor.firstValue.onAuthStateChanged(mockAuth)

      // Assert
      assert(!callbackTriggered) {
        "The onSuccess callback should not be triggered when user is not logged in."
      }
    }
  }

  // Test for successful conversion with valid data
  @Test
  fun documentToQuest_withValidData_returnsQuest() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn("Quest Description")
    `when`(mockDocument.getString("index")).thenReturn("1")

    val quest = firestoreQuestRepository.documentToQuest(mockDocument)

    assertEquals("Quest Title", quest?.title)
    assertEquals("Quest Description", quest?.description)
    assertEquals("1", quest?.index)
  }

  @Test
  fun documentToQuest_withMissingTitle_returnsNull() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn(null) // Missing title
    `when`(mockDocument.getString("description")).thenReturn("Quest Description")
    `when`(mockDocument.getString("index")).thenReturn("1")

    val quest = firestoreQuestRepository.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_withMissingIndex_returnsNull() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn("Quest Description")
    `when`(mockDocument.getString("index")).thenReturn(null) // Missing index

    val quest = firestoreQuestRepository.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_withMissingDescription_returnsNull() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn(null) // Missing description
    `when`(mockDocument.getString("index")).thenReturn("1")

    val quest = firestoreQuestRepository.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_throwsException_returnsNull() {
    // Arrange: Create a mock DocumentSnapshot that throws an exception when getting a field
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString(any())).thenThrow(RuntimeException("Test Exception"))

    // Act
    val quest = firestoreQuestRepository.documentToQuest(mockDocument)

    // Assert
    assertNull(quest)
  }

  @Test
  fun getDailyQuest_successfulFetch_returnsSortedQuests() {
    // Arrange: Create mock DocumentSnapshots with valid data and out-of-order indices
    val mockDocument1 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument1.getString("title")).thenReturn("Quest Title 3")
    `when`(mockDocument1.getString("description")).thenReturn("Quest Description 3")
    `when`(mockDocument1.getString("index")).thenReturn("3")

    val mockDocument2 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument2.getString("title")).thenReturn("Quest Title 1")
    `when`(mockDocument2.getString("description")).thenReturn("Quest Description 1")
    `when`(mockDocument2.getString("index")).thenReturn("1")

    val mockDocument3 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument3.getString("title")).thenReturn("Quest Title 2")
    `when`(mockDocument3.getString("description")).thenReturn("Quest Description 2")
    `when`(mockDocument3.getString("index")).thenReturn("2")

    `when`(mockQuestQuerySnapshot.documents)
        .thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act & Assert
    firestoreQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify that quests are sorted by index
          assertEquals(3, quests.size)
          assertEquals("Quest Title 1", quests[0].title)
          assertEquals("Quest Title 2", quests[1].title)
          assertEquals("Quest Title 3", quests[2].title)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  @Test
  fun getDailyQuest_emptyDocumentList_returnsEmptyList() {
    // Arrange: Simulate an empty QuerySnapshot
    `when`(mockQuestQuerySnapshot.documents).thenReturn(emptyList())
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act & Assert
    firestoreQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify that an empty list is returned
          assertEquals(0, quests.size)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  @Test
  fun getDailyQuest_nullTaskResult_returnsEmptyList() {
    // Arrange: Simulate a null task result
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(null))

    // Act & Assert
    firestoreQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify that an empty list is returned when task result is null
          assertEquals(0, quests.size)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  @Test
  fun getDailyQuest_taskSuccessful() {
    // Arrange: Mock task completion to simulate a successful task
    `when`(mockCollectionReference.get()).thenReturn(mockTask)
    `when`(mockTask.isSuccessful).thenReturn(true)

    // Mock QuerySnapshot to return a list containing distinct DocumentSnapshots
    `when`(mockQuestQuerySnapshot.documents).thenReturn(listOf())

    // Act
    firestoreQuestRepository.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify quests are sorted by index
          assertEquals(0, quests.size)
        },
        onFailure = { fail("Expected success but got failure") })

    // Capture and invoke the OnCompleteListener to simulate task completion
    verify(mockTask).addOnCompleteListener(captor.capture())
    captor.value.onComplete(mockTask)
  }

  @Test
  fun getDailyQuest_taskUnsuccessful_callsOnFailure() {
    // Arrange
    `when`(mockCollectionReference.get()).thenReturn(mockTask)
    `when`(mockTask.isSuccessful).thenReturn(false)
    `when`(mockTask.exception).thenReturn(Exception("Firestore error"))

    firestoreQuestRepository.getDailyQuest(
        onSuccess = { fail("Expected failure but got success.") },
        onFailure = { error -> assertEquals("Firestore error", error.message) })

    // Capture and invoke the OnCompleteListener
    verify(mockTask).addOnCompleteListener(captor.capture())
    captor.value.onComplete(mockTask)
  }

  @Test
  fun fetchVideo_constructsCorrectPath() {
    // construct video path
    val videoPath = questRepositoryFirestore.fetchVideo("Thank You")

    // Assert
    assertEquals("android.resource://com.github.se.signify/raw/thankyou", videoPath)
  }
}
