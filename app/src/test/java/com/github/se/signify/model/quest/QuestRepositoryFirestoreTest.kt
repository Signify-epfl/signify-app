package com.github.se.signify.model.quest

import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.timeout
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QuestRepositoryFirestoreTest {
  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockDocumentReference: DocumentReference
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockQuestQuerySnapshot: QuerySnapshot

  private lateinit var mockAuth: FirebaseAuth
  private lateinit var mockUser: FirebaseUser

  private lateinit var questRepositoryFirestore: QuestRepositoryFireStore

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    questRepositoryFirestore = QuestRepositoryFireStore(mockFirestore)

    `when`(mockFirestore.collection(any())).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(any())).thenReturn(mockDocumentReference)
    `when`(mockCollectionReference.document()).thenReturn(mockDocumentReference)

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
      questRepositoryFirestore.init {
        callbackTriggered = true // Set flag if onSuccess is triggered
      }

      // Simulate Firebase invoking the auth state listener
      authStateListenerCaptor.firstValue.onAuthStateChanged(mockAuth)

      // Assert
      assert(callbackTriggered) { "The onSuccess callback was not triggered." }
    }
  }

  @Test
  fun getDailyQuest_callsDocuments() {
    // Ensure that mockToDoQuerySnapshot is properly initialized and mocked
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Ensure the QuerySnapshot returns a list of mock DocumentSnapshots
    `when`(mockQuestQuerySnapshot.documents).thenReturn(listOf())

    // Call the method under test
    questRepositoryFirestore.getDailyQuest(
        onSuccess = {}, onFailure = { fail("Failure callback should not be called") })

    // Verify that the 'documents' field was accessed
    verify(timeout(100)) { (mockQuestQuerySnapshot).documents }
  }

  @Test
  fun getDailyQuest_successfulConversion() {
    // Arrange
    val mockDocumentSnapshot1: DocumentSnapshot = mock(DocumentSnapshot::class.java)
    `when`(mockDocumentSnapshot1.getString("title")).thenReturn("Quest Title 1")
    `when`(mockDocumentSnapshot1.getString("description")).thenReturn("Quest Description 1")
    `when`(mockDocumentSnapshot1.getString("index")).thenReturn("1")

    val mockDocumentSnapshot2: DocumentSnapshot = mock(DocumentSnapshot::class.java)
    `when`(mockDocumentSnapshot2.getString("title")).thenReturn("Quest Title 2")
    `when`(mockDocumentSnapshot2.getString("description")).thenReturn("Quest Description 2")
    `when`(mockDocumentSnapshot2.getString("index")).thenReturn("2")

    `when`(mockQuestQuerySnapshot.documents)
        .thenReturn(listOf(mockDocumentSnapshot1, mockDocumentSnapshot2))
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify that quests are correctly parsed
          assertEquals(2, quests.size)
          assertEquals("Quest Title 1", quests[0].title)
          assertEquals("Quest Description 1", quests[0].description)
          assertEquals("1", quests[0].index)

          assertEquals("Quest Title 2", quests[1].title)
          assertEquals("Quest Description 2", quests[1].description)
          assertEquals("2", quests[1].index)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  // Test for successful conversion with valid data
  @Test
  fun documentToQuest_withValidData_returnsQuest() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn("Quest Description")
    `when`(mockDocument.getString("index")).thenReturn("1")

    val quest = questRepositoryFirestore.documentToQuest(mockDocument)

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

    val quest = questRepositoryFirestore.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_withMissingIndex_returnsNull() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn("Quest Description")
    `when`(mockDocument.getString("index")).thenReturn(null) // Missing index

    val quest = questRepositoryFirestore.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_withMissingDescription_returnsNull() {
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString("title")).thenReturn("Quest Title")
    `when`(mockDocument.getString("description")).thenReturn(null) // Missing description
    `when`(mockDocument.getString("index")).thenReturn("1")

    val quest = questRepositoryFirestore.documentToQuest(mockDocument)

    assertNull(quest)
  }

  @Test
  fun documentToQuest_throwsException_returnsNull() {
    // Arrange: Create a mock DocumentSnapshot that throws an exception when getting a field
    val mockDocument = mock(DocumentSnapshot::class.java)
    `when`(mockDocument.getString(any())).thenThrow(RuntimeException("Test Exception"))

    // Act
    val quest = questRepositoryFirestore.documentToQuest(mockDocument)

    // Assert
    assertNull(quest)
  }

  @Test
  fun getDailyQuest_triggersFailureLambda() {
    // Arrange: Simulate a Firestore failure
    val exception =
        FirebaseFirestoreException("Firestore error", FirebaseFirestoreException.Code.ABORTED)
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forException(exception))

    // Act & Assert
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { fail("Success callback should not be called") },
        onFailure = { error ->
          // Assert: Verify that the onFailure callback is triggered with the correct exception
          assertEquals("Firestore error", error.message)
        })
  }

  @Test
  fun getDailyQuest_successfulFetch_callsOnSuccessWithQuests() {
    // Arrange: Mock DocumentSnapshots
    val mockDocument1 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument1.getString("title")).thenReturn("Quest Title 1")
    `when`(mockDocument1.getString("description")).thenReturn("Quest Description 1")
    `when`(mockDocument1.getString("index")).thenReturn("1")

    val mockDocument2 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument2.getString("title")).thenReturn("Quest Title 2")
    `when`(mockDocument2.getString("description")).thenReturn("Quest Description 2")
    `when`(mockDocument2.getString("index")).thenReturn("2")

    `when`(mockQuestQuerySnapshot.documents).thenReturn(listOf(mockDocument1, mockDocument2))
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act & Assert
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { quests ->
          assertEquals(2, quests.size)
          assertEquals("Quest Title 1", quests[0].title)
          assertEquals("Quest Description 1", quests[0].description)
          assertEquals("1", quests[0].index)

          assertEquals("Quest Title 2", quests[1].title)
          assertEquals("Quest Description 2", quests[1].description)
          assertEquals("2", quests[1].index)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  @Test
  fun getDailyQuest_failedFetch_callsOnFailure() {
    // Arrange: Simulate a Firestore query failure
    val exception =
        FirebaseFirestoreException("Firestore error", FirebaseFirestoreException.Code.ABORTED)
    val failedTask: Task<QuerySnapshot> = Tasks.forException(exception)
    `when`(mockCollectionReference.get()).thenReturn(failedTask)

    // Act & Assert
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { fail("Success callback should not be called") },
        onFailure = { error -> assertEquals("Firestore error", error.message) })
  }

  @Test
  fun getDailyQuest_success_callsOnSuccessWithQuests() {
    // Arrange
    val mockDocument1 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument1.getString("title")).thenReturn("Quest Title 1")
    `when`(mockDocument1.getString("description")).thenReturn("Quest Description 1")
    `when`(mockDocument1.getString("index")).thenReturn("1")

    val mockDocument2 = mock(DocumentSnapshot::class.java)
    `when`(mockDocument2.getString("title")).thenReturn("Quest Title 2")
    `when`(mockDocument2.getString("description")).thenReturn("Quest Description 2")
    `when`(mockDocument2.getString("index")).thenReturn("2")

    `when`(mockQuestQuerySnapshot.documents).thenReturn(listOf(mockDocument1, mockDocument2))
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { quests ->
          assertEquals(2, quests.size)
          assertEquals("Quest Title 1", quests[0].title)
          assertEquals("Quest Description 1", quests[0].description)
          assertEquals("1", quests[0].index)

          assertEquals("Quest Title 2", quests[1].title)
          assertEquals("Quest Description 2", quests[1].description)
          assertEquals("2", quests[1].index)
        },
        onFailure = { fail("Failure callback should not be called") })
  }

  @Test
  fun getDailyQuest_failure_callsOnFailureWithException() {
    // Arrange
    val exception =
        FirebaseFirestoreException("Firestore error", FirebaseFirestoreException.Code.ABORTED)
    val failedTask: Task<QuerySnapshot> = Tasks.forException(exception)
    `when`(mockCollectionReference.get()).thenReturn(failedTask)

    // Act
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { fail("Success callback should not be called") },
        onFailure = { error -> assertEquals("Firestore error", error.message) })
  }

  @Test
  fun getDailyQuest_sortsQuestsByIndex() {
    // Arrange: Create mock DocumentSnapshots with out-of-order indices
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

    // Mock the QuerySnapshot to return these documents
    `when`(mockQuestQuerySnapshot.documents)
        .thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
    `when`(mockCollectionReference.get()).thenReturn(Tasks.forResult(mockQuestQuerySnapshot))

    // Act
    questRepositoryFirestore.getDailyQuest(
        onSuccess = { quests ->
          // Assert: Verify that quests are sorted by the index field
          assertEquals(3, quests.size)
          assertEquals("Quest Title 1", quests[0].title)
          assertEquals("Quest Title 2", quests[1].title)
          assertEquals("Quest Title 3", quests[2].title)
        },
        onFailure = { fail("Failure callback should not be called") })
  }
}
