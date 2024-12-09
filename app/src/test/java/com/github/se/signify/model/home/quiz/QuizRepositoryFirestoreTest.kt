package com.github.se.signify.model.home.quiz

import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QuizRepositoryFireStoreTest {

  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockQuerySnapshot: QuerySnapshot
  @Mock private lateinit var mockTask: Task<QuerySnapshot>
  @Captor private lateinit var captor: ArgumentCaptor<OnCompleteListener<QuerySnapshot>>

  private lateinit var quizRepositoryFireStore: QuizRepositoryFireStore
  private lateinit var mockAuth: FirebaseAuth
  private lateinit var mockUser: FirebaseUser

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize Firebase if necessary
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    // Instantiate the QuizRepositoryFireStore
    quizRepositoryFireStore = QuizRepositoryFireStore(mockFirestore)

    // Mock Firestore interactions
    `when`(mockFirestore.collection(any())).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(any())).thenReturn(mock(DocumentReference::class.java))
    `when`(mockCollectionReference.get()).thenReturn(mockTask)

    // Mock QuerySnapshot and DocumentSnapshot
    `when`(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot))
    `when`(mockTask.result).thenReturn(mockQuerySnapshot)

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
      quizRepositoryFireStore.init {
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
    mockStatic(FirebaseAuth::class.java).use { firebaseAuthMock ->
      firebaseAuthMock.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(mockAuth)
      `when`(mockAuth.currentUser).thenReturn(null) // No user logged in

      var callbackTriggered = false
      quizRepositoryFireStore.init { callbackTriggered = true }

      verify(mockAuth).addAuthStateListener(any())
      assert(!callbackTriggered) {
        "Expected onSuccess not to be triggered when user is not logged in."
      }
    }
  }

  @Test
  fun getQuizQuestions_unsuccessfulFetch_callsOnFailure() {
    val exception = Exception("Firestore error")
    `when`(mockTask.isSuccessful).thenReturn(false)
    `when`(mockTask.exception).thenReturn(exception)
    `when`(mockCollectionReference.get()).thenReturn(mockTask)

    quizRepositoryFireStore.getQuizQuestions(
        onSuccess = { fail("onSuccess should not be called.") },
        onFailure = { error -> assertEquals("Firestore error", error.message) })

    verify(mockTask).addOnCompleteListener(captor.capture())
    captor.value.onComplete(mockTask)
  }

  @Test
  fun documentToQuiz_withValidData_returnsQuizQuestion() {
    `when`(mockDocumentSnapshot.getString("word")).thenReturn("car")
    `when`(mockDocumentSnapshot.get("confusers")).thenReturn(listOf("cat", "cup"))

    val quiz = quizRepositoryFireStore.documentToQuiz(mockDocumentSnapshot)

    assertEquals("car", quiz?.correctWord)
    assertEquals(listOf("cat", "cup"), quiz?.confusers)
  }

  @Test
  fun documentToQuiz_withMissingWord_returnsNull() {
    `when`(mockDocumentSnapshot.getString("word")).thenReturn(null)

    val quiz = quizRepositoryFireStore.documentToQuiz(mockDocumentSnapshot)

    assertNull(quiz)
  }

  @Test
  fun documentToQuiz_withInvalidConfusers_returnsNull() {
    `when`(mockDocumentSnapshot.getString("word")).thenReturn("Apple")
    `when`(mockDocumentSnapshot.get("confusers")).thenReturn("Invalid Data")

    val quiz = quizRepositoryFireStore.documentToQuiz(mockDocumentSnapshot)

    assertNull(quiz)
  }

  @Test
  fun documentToQuiz_withException_logsErrorAndReturnsNull() {
    `when`(mockDocumentSnapshot.getString("word")).thenThrow(RuntimeException("Test Exception"))

    val quiz = quizRepositoryFireStore.documentToQuiz(mockDocumentSnapshot)

    assertNull(quiz)
  }
}
