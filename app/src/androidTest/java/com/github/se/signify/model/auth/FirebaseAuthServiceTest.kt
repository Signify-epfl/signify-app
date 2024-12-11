package com.github.se.signify.model.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.authentication.FirebaseAuthService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.asTask
import org.junit.Test
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.times

@Suppress("DEPRECATION")
class FirebaseAuthServiceTest {

  @Test
  fun signInWithGoogleReturnTrue() = runBlocking {
    val idToken = "mock-id-token"
    val mockCredential = mock(AuthCredential::class.java)
    val mockAuthResult = mock(AuthResult::class.java)
    val mockFirebaseUser = mock(FirebaseUser::class.java)

    val mockFirebaseAuth = mock(FirebaseAuth::class.java)

    val mockSignInTask = CompletableDeferred(mockAuthResult).asTask()
    `when`(mockFirebaseAuth.signInWithCredential(mockCredential)).thenReturn(mockSignInTask)

    `when`(mockAuthResult.user).thenReturn(mockFirebaseUser)

    val credentialProvider = mock<(String, String?) -> AuthCredential>()
    `when`(credentialProvider(eq(idToken), eq(null))).thenReturn(mockCredential)

    val firebaseAuthService =
        FirebaseAuthService(
            firebaseAuth = mockFirebaseAuth, credentialProvider = credentialProvider)
    val result = firebaseAuthService.signInWithGoogle(idToken)

    assertTrue("signInWithGoogle should return true when user is authenticated", result)
  }

  @Test
  fun handleAuthResultCallsOnAuthCompleteOnSuccess() {
    val mockIntent = mock(Intent::class.java)
    val mockGoogleSignInAccount = mock(GoogleSignInAccount::class.java)
    val mockCredential = mock(AuthCredential::class.java)
    val mockAuthResult = mock(AuthResult::class.java)

    val mockResult =
        FirebaseAuthService.ActivityResultWrapper(
            resultCode = Activity.RESULT_OK, data = mockIntent)

    val onAuthComplete = mock<(AuthResult) -> Unit>()
    val onAuthError = mock<(ApiException) -> Unit>()

    val googleSignInHelper = mock<(Intent) -> Task<GoogleSignInAccount>>()
    val mockGoogleTask = mock(Task::class.java) as Task<GoogleSignInAccount>
    `when`(googleSignInHelper(mockIntent)).thenReturn(mockGoogleTask)
    `when`(mockGoogleTask.getResult(ApiException::class.java)).thenReturn(mockGoogleSignInAccount)

    `when`(mockGoogleSignInAccount.idToken).thenReturn("mock-id-token")

    val credentialProvider = mock<(String, String?) -> AuthCredential>()
    `when`(credentialProvider("mock-id-token", null)).thenReturn(mockCredential)

    val mockFirebaseAuth = mock(FirebaseAuth::class.java)
    val mockSignInTask = mock(Task::class.java) as Task<AuthResult>

    doAnswer { invocation ->
          val onSuccessListener = invocation.arguments[0] as OnSuccessListener<AuthResult>
          onSuccessListener.onSuccess(mockAuthResult)
          mockSignInTask
        }
        .`when`(mockSignInTask)
        .addOnSuccessListener(any())

    doAnswer { mockSignInTask }.`when`(mockSignInTask).addOnFailureListener(any())

    `when`(mockFirebaseAuth.signInWithCredential(mockCredential)).thenReturn(mockSignInTask)

    val firebaseAuthService =
        FirebaseAuthService(
            firebaseAuth = mockFirebaseAuth,
            googleSignInHelper = googleSignInHelper,
            credentialProvider = credentialProvider)

    firebaseAuthService.handleAuthResult(
        result = mockResult, onAuthComplete = onAuthComplete, onAuthError = onAuthError)

    verify(onAuthComplete, times(1)).invoke(mockAuthResult)
    verify(onAuthError, times(0)).invoke(any())
  }

  @Test
  fun signOutWithGoogleReturnsTrue() = runBlocking {
    val mockFirebaseAuth = mock(FirebaseAuth::class.java)
    val firebaseAuthService = FirebaseAuthService(mockFirebaseAuth)

    val result = firebaseAuthService.signOut()

    assertTrue("signOut should return true", result)
    verify(mockFirebaseAuth, times(1)).signOut()
  }

  @Test
  fun getCurrentUserReturnsCurrentUser() {

    val mockFirebaseAuth = mock(FirebaseAuth::class.java)
    val mockFirebaseUser = mock(FirebaseUser::class.java)

    `when`(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)
    `when`(mockFirebaseUser.email).thenReturn("mock-token@example.com")

    val firebaseAuthService = FirebaseAuthService(mockFirebaseAuth)

    val currentUserEmail = firebaseAuthService.getCurrentUser()

    assertEquals("mock-token@example.com", currentUserEmail)
  }

  @Test
  fun handleAuthResultInvalidActivityResultCallsOnAuthError() {
    val firebaseAuthService = FirebaseAuthService()
    val mockIntent: Intent? = null
    val mockResult =
        FirebaseAuthService.ActivityResultWrapper(
            resultCode = Activity.RESULT_CANCELED, data = mockIntent)
    val onAuthComplete = mock<(AuthResult) -> Unit>()
    val onAuthError = mock<(ApiException) -> Unit>()

    firebaseAuthService.handleAuthResult(
        result = mockResult, onAuthComplete = onAuthComplete, onAuthError = onAuthError)
    verify(onAuthError, times(1)).invoke(any<ApiException>())
    verify(onAuthComplete, times(0)).invoke(any())
  }

  @Test
  fun handleAuthResultWithNullIdTokenCallsOnAuthError() {
    val mockIntent = mock(Intent::class.java)
    val mockTask = mock(Task::class.java) as Task<GoogleSignInAccount>
    val mockGoogleSignInAccount = mock(GoogleSignInAccount::class.java)
    val mockResult =
        FirebaseAuthService.ActivityResultWrapper(
            resultCode = Activity.RESULT_OK, data = mockIntent)
    val onAuthComplete = mock<(AuthResult) -> Unit>()
    val onAuthError = mock<(ApiException) -> Unit>()

    val googleSignInHelper = mock<(Intent) -> Task<GoogleSignInAccount>>()
    `when`(googleSignInHelper(mockIntent)).thenReturn(mockTask)

    `when`(mockTask.isSuccessful).thenReturn(true)
    `when`(mockTask.result).thenReturn(mockGoogleSignInAccount)

    `when`(mockGoogleSignInAccount.idToken).thenReturn(null)

    val firebaseAuthService = FirebaseAuthService(FirebaseAuth.getInstance(), googleSignInHelper)

    firebaseAuthService.handleAuthResult(
        result = mockResult, onAuthComplete = onAuthComplete, onAuthError = onAuthError)

    verify(onAuthError, times(1)).invoke(any<ApiException>())
    verify(onAuthComplete, times(0)).invoke(any())
  }

  @Test
  fun handleAuthResultThrowsApiExceptionCallsOnAuthError() {
    val mockIntent = mock(Intent::class.java)
    val mockTask = mock(Task::class.java) as Task<GoogleSignInAccount>
    val mockResult =
        FirebaseAuthService.ActivityResultWrapper(
            resultCode = Activity.RESULT_OK, data = mockIntent)
    val onAuthComplete = mock<(AuthResult) -> Unit>()
    val onAuthError = mock<(ApiException) -> Unit>()

    `when`(mockTask.isSuccessful).thenReturn(false)
    `when`(mockTask.exception).thenReturn(ApiException(Status.RESULT_INTERNAL_ERROR))

    val googleSignInHelper = mock<(Intent) -> Task<GoogleSignInAccount>>()
    `when`(googleSignInHelper(mockIntent)).thenReturn(mockTask)

    val firebaseAuthService = FirebaseAuthService(FirebaseAuth.getInstance(), googleSignInHelper)

    firebaseAuthService.handleAuthResult(
        result = mockResult, onAuthComplete = onAuthComplete, onAuthError = onAuthError)
    verify(onAuthError, times(1)).invoke(any<ApiException>())
    verify(onAuthComplete, times(0)).invoke(any())
  }

  @Test
  fun initializeGoogleSignInClientConfiguresClientCorrectly() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val token = "mock-token"

    val firebaseAuthService = FirebaseAuthService()

    firebaseAuthService.initializeGoogleSignInClient(context, token)

    assertNotNull(firebaseAuthService.googleSignInClient)
  }

  @Test
  fun handleAuthResultCallsOnAuthErrorWhenGoogleSignInFails() {
    // Arrange
    val mockIntent = mock(Intent::class.java)
    val mockTask = mock(Task::class.java) as Task<GoogleSignInAccount>
    val mockResult =
        FirebaseAuthService.ActivityResultWrapper(
            resultCode = android.app.Activity.RESULT_OK, data = mockIntent)
    val onAuthComplete = mock<(AuthResult) -> Unit>()
    val onAuthError = mock<(ApiException) -> Unit>()

    // Mock the behavior to throw an ApiException
    val googleSignInHelper = mock<(Intent) -> Task<GoogleSignInAccount>>()
    `when`(googleSignInHelper(mockIntent)).thenReturn(mockTask)
    `when`(mockTask.getResult(ApiException::class.java))
        .thenThrow(ApiException(Status.RESULT_INTERNAL_ERROR))

    val firebaseAuthService = FirebaseAuthService(googleSignInHelper = googleSignInHelper)

    // Act
    firebaseAuthService.handleAuthResult(
        result = mockResult, onAuthComplete = onAuthComplete, onAuthError = onAuthError)

    // Assert
    verify(onAuthError, times(1)).invoke(any<ApiException>())
    verify(onAuthComplete, times(0)).invoke(any())
  }
}
