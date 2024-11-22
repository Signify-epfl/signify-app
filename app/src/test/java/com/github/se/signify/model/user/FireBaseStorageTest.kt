package com.github.se.signify.model.user

import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FireBaseStorageTest {

  @Mock lateinit var mockFireStore: FirebaseFirestore

  @Mock lateinit var mockStorage: FirebaseStorage

  @Mock lateinit var mockStorageReference: StorageReference

  @Mock lateinit var mockDocumentReference: DocumentReference

  private lateinit var repository: UserRepositoryFireStore
  private val users = "users"
  private val currentUserId = "currentUserId"

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    // Initialize FirebaseApp
    if (FirebaseApp.getApps(ApplicationProvider.getApplicationContext()).isEmpty()) {
      FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
    }

    // Mock FirebaseStorage
    `when`(mockStorage.reference).thenReturn(mockStorageReference)
    `when`(mockStorageReference.child(anyString())).thenReturn(mockStorageReference)

    // Mock FireStore
    `when`(mockFireStore.collection(users)).thenReturn(mock(CollectionReference::class.java))
    `when`(mockFireStore.collection(users).document(anyString())).thenReturn(mockDocumentReference)

    // Create repository
    repository = UserRepositoryFireStore(mockFireStore, mockStorage)
  }

  @Suppress("UNCHECKED_CAST")
  @Test
  fun updateProfilePictureUrlSuccess() {
    val mockUri = mock(Uri::class.java)
    val mockUploadTask = mock(UploadTask::class.java)
    val mockTaskSnapshot = mock(UploadTask.TaskSnapshot::class.java)
    val mockDownloadUrlTask = mock(Task::class.java) as Task<Uri>
    val mockDownloadUri = Uri.parse("https://example.com/mockimage.jpg")

    // Mock StorageReference behavior
    `when`(mockStorageReference.putFile(mockUri)).thenReturn(mockUploadTask)
    `when`(mockUploadTask.addOnSuccessListener(any())).thenAnswer {
      (it.arguments[0] as OnSuccessListener<UploadTask.TaskSnapshot>).onSuccess(mockTaskSnapshot)
      mockUploadTask
    }
    `when`(mockStorageReference.downloadUrl).thenReturn(mockDownloadUrlTask)
    `when`(mockDownloadUrlTask.addOnSuccessListener(any())).thenAnswer {
      (it.arguments[0] as OnSuccessListener<Uri>).onSuccess(mockDownloadUri)
      mockDownloadUrlTask
    }

    val onSuccess = mock<(Unit) -> Unit>()
    val onFailure = mock<(Exception) -> Unit>()

    repository.updateProfilePictureUrl(
        currentUserId, mockUri, onSuccess = { onSuccess(Unit) }, onFailure)

    // Verify interactions
    verify(mockStorageReference).putFile(mockUri)
    verify(mockStorageReference).downloadUrl
    verify(onFailure, never()).invoke(any())
  }

  @Test
  fun updateProfilePictureUrlFailsUpload() {
    val mockUri = mock(Uri::class.java)
    val mockUploadTask = mock(UploadTask::class.java)
    val mockException = Exception("Upload failed")

    // Mock StorageReference behavior
    `when`(mockStorageReference.putFile(mockUri)).thenReturn(mockUploadTask)
    `when`(mockUploadTask.addOnSuccessListener(any())).thenReturn(mockUploadTask) // Allow chaining
    `when`(mockUploadTask.addOnFailureListener(any())).thenAnswer {
      (it.arguments[0] as OnFailureListener).onFailure(mockException)
      mockUploadTask
    }

    val onSuccess = mock<(Unit) -> Unit>()
    val onFailure = mock<(Exception) -> Unit>()

    repository.updateProfilePictureUrl(
        currentUserId, mockUri, onSuccess = { onSuccess(Unit) }, onFailure)

    // Verify interactions
    verify(mockStorageReference).putFile(mockUri)
    verify(onFailure).invoke(mockException)
    verify(onSuccess, never()).invoke(any())
  }

  @Suppress("UNCHECKED_CAST")
  @Test
  fun updateProfilePictureUrlFailsDownloadUrl() {
    val mockUri = mock(Uri::class.java)
    val mockUploadTask = mock(UploadTask::class.java)
    val mockTaskSnapshot = mock(UploadTask.TaskSnapshot::class.java)
    val mockDownloadUrlTask = mock(Task::class.java) as Task<Uri>
    val mockException = Exception("Failed to retrieve download URL")

    // Mock StorageReference behavior
    `when`(mockStorageReference.putFile(mockUri)).thenReturn(mockUploadTask)
    `when`(mockUploadTask.addOnSuccessListener(any())).thenAnswer {
      (it.arguments[0] as OnSuccessListener<UploadTask.TaskSnapshot>).onSuccess(mockTaskSnapshot)
      mockUploadTask // Return the mocked `UploadTask` for chaining
    }
    `when`(mockUploadTask.addOnFailureListener(any())).thenReturn(mockUploadTask)

    // Mock download URL failure
    `when`(mockStorageReference.downloadUrl).thenReturn(mockDownloadUrlTask)
    `when`(mockDownloadUrlTask.addOnSuccessListener(any())).thenReturn(mockDownloadUrlTask)
    `when`(mockDownloadUrlTask.addOnFailureListener(any())).thenAnswer {
      (it.arguments[0] as OnFailureListener).onFailure(mockException)
      mockDownloadUrlTask
    }

    val onSuccess = mock<(Unit) -> Unit>()
    val onFailure = mock<(Exception) -> Unit>()

    repository.updateProfilePictureUrl(
        currentUserId, mockUri, onSuccess = { onSuccess(Unit) }, onFailure)

    // Verify interactions
    verify(mockStorageReference).putFile(mockUri)
    verify(mockStorageReference).downloadUrl
    verify(onFailure).invoke(mockException)
    verify(onSuccess, never()).invoke(any())
  }
}
