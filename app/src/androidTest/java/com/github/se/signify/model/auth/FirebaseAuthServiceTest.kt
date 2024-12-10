package com.github.se.signify.model.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.authentication.FirebaseAuthService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.times

@Suppress("DEPRECATION")
class FirebaseAuthServiceTest {

  private lateinit var firebaseAuthService: FirebaseAuthService

  @Before
  fun setUp() {
    firebaseAuthService = FirebaseAuthService()
  }

  @Test
  fun signInWithGoogleReturnsTrue() = runBlocking {
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
}
