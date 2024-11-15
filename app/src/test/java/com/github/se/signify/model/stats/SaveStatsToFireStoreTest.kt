package com.github.se.signify.model.stats

import androidx.test.core.app.ApplicationProvider
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

@RunWith(RobolectricTestRunner::class)
class SaveStatsToFireStoreTest {

  @Mock private lateinit var mockAuth: FirebaseAuth
  @Mock private lateinit var mockCurrentUser: FirebaseUser
  @Mock private lateinit var mockFirestore: FirebaseFirestore
  @Mock private lateinit var mockCollectionReference: CollectionReference
  @Mock private lateinit var mockUserDocument: DocumentReference

  @Before
  fun setup() {
    MockitoAnnotations.openMocks(this)

    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    mockAuth = mock(FirebaseAuth::class.java)
    mockCurrentUser = mock(FirebaseUser::class.java)
    mockFirestore = mock(FirebaseFirestore::class.java)
    mockCollectionReference = mock(CollectionReference::class.java)
    mockUserDocument = mock(DocumentReference::class.java)

    // Mock Firestore document reference to return userDocument for any document request
    `when`(mockFirestore.collection(anyString())).thenReturn(mockCollectionReference)
  }

  @Test
  fun saveUserStatsToFirestoreShouldAddStatsIfDocumentDoesNotExist() {
    val documentSnapshot = mock(DocumentSnapshot::class.java)
    `when`(documentSnapshot.exists()).thenReturn(false)

    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(documentSnapshot))

    saveStatsToFirestore()

    `when`(mockUserDocument.set(any(), eq(SetOptions.merge()))).thenReturn(Tasks.forResult(null))
  }

  @Test
  fun saveUserStatsToFirestoreShouldNotAddUserStatsIfDocumentAlreadyExists() {
    val documentSnapshot = mock(DocumentSnapshot::class.java)
    `when`(documentSnapshot.exists()).thenReturn(true)

    `when`(mockUserDocument.get()).thenReturn(Tasks.forResult(documentSnapshot))

    saveStatsToFirestore()

    verify(mockUserDocument, never()).set(any(), any())
  }

  @Test
  fun saveUserStatsToFirestoreShouldLogErrorIfThereIsAnError() {
    `when`(mockAuth.currentUser).thenReturn(null)

    saveStatsToFirestore()

    verify(mockFirestore, never()).collection(any())
  }
}
