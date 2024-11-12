package com.github.se.signify.model.quest

import androidx.test.core.app.ApplicationProvider
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
      questRepositoryFirestore.init {
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
}
