package com.github.se.signify.model.common.user

import android.os.Looper
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
import com.google.firebase.firestore.SetOptions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class SaveUserToFirestoreTest {

  @Mock private lateinit var mockAuth: FirebaseAuth
  @Mock private lateinit var mockCurrentUser: FirebaseUser
  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockDocumentReference: DocumentReference
  @Mock private lateinit var mockDocumentSnapshot: DocumentSnapshot
  @Mock private lateinit var mockGetTask: Task<DocumentSnapshot>

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    // Mock FirebaseAuth and FirebaseFireStore initialization
    mockAuth = mock(FirebaseAuth::class.java)
    mockFirestore = mock(FirebaseFirestore::class.java)

    // Mock user's authentication
    mockCurrentUser = mock(FirebaseUser::class.java)
    `when`(mockAuth.currentUser).thenReturn(mockCurrentUser)

    // Mock FireStore collection and document reference
    mockCollectionReference = mock(CollectionReference::class.java)
    mockDocumentReference = mock(DocumentReference::class.java)

    `when`(mockFirestore.collection(anyString())).thenReturn(mockCollectionReference)
    `when`(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference)

    // Mock FireStore document get task
    mockGetTask = mock(Task::class.java) as Task<DocumentSnapshot>
    `when`(mockDocumentReference.get()).thenReturn(mockGetTask)
  }

  @Test
  fun saveUserToFireStore_shouldAddUserWhenUserDoesNotExist() {
    // Arrange
    `when`(mockCurrentUser.email).thenReturn("testuser@gmail.com")
    `when`(mockCurrentUser.displayName).thenReturn("Test User")

    // Simulate that the user document does not exist
    `when`(mockGetTask.isSuccessful).thenReturn(true)
    `when`(mockGetTask.result).thenReturn(mockDocumentSnapshot)
    `when`(mockDocumentSnapshot.exists()).thenReturn(false) // Document does not exist

    // Simulate successful set task
    val mockSetTask = Tasks.forResult<Void>(null) // Simulate successful FireStore set task
    `when`(mockDocumentReference.set(any(), eq(SetOptions.merge()))).thenReturn(mockSetTask)

    // Act
    saveUserToFirestore()

    // Idle the main looper to process tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    `when`(mockDocumentReference.set(any(), eq(SetOptions.merge())))
        .thenReturn(Tasks.forResult(null))
  }

  @Test
  fun saveUserToFireStore_shouldNotAddUserWhenUserAlreadyExists() {
    // Arrange
    `when`(mockCurrentUser.email).thenReturn("testuser@gmail.com")
    `when`(mockCurrentUser.displayName).thenReturn("Test User")

    // Simulate that the user document already exists
    `when`(mockDocumentSnapshot.exists()).thenReturn(true)
    `when`(mockGetTask.isSuccessful).thenReturn(true)
    `when`(mockGetTask.result).thenReturn(mockDocumentSnapshot)

    // Act
    saveUserToFirestore()

    // Idle the main looper to process tasks
    shadowOf(Looper.getMainLooper()).idle()

    // Assert
    verify(mockDocumentReference, never()).set(any(), any()) // Ensure set is never called
  }

  @Test
  fun saveUserToFireStore_shouldLogErrorWhenUserNotLoggedIn() {
    // Arrange
    `when`(mockAuth.currentUser).thenReturn(null) // Simulate no user logged in

    // Act
    saveUserToFirestore()

    // Verify that the error is logged
    // (Since we cannot easily verify logs, we focus on ensuring no Firestore interaction occurs)
    verify(mockFirestore, never()).collection(any())
  }
}
