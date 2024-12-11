@file:Suppress("DEPRECATION")

package com.github.se.signify.model.authentication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * `FirebaseAuthService` provides an implementation of the `AuthService` interface, leveraging
 * Firebase Authentication for user sign-in and sign-out operations. It integrates with Google
 * Sign-In for user authentication.
 *
 * @property firebaseAuth The FirebaseAuth instance used for authentication.
 * @property googleSignInHelper A helper function for handling Google Sign-In results.
 * @property credentialProvider A provider function for generating Firebase authentication
 *   credentials.
 */
class FirebaseAuthService(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val googleSignInHelper: (Intent) -> Task<GoogleSignInAccount> = { intent ->
      GoogleSignIn.getSignedInAccountFromIntent(intent)
    },
    private val credentialProvider: (String, String?) -> AuthCredential = { idToken, secret ->
      GoogleAuthProvider.getCredential(idToken, secret)
    }
) : AuthService {
  /**
   * GoogleSignInClient for managing Google Sign-In operations. It is initialized lazily via
   * `initializeGoogleSignInClient`.
   */
  lateinit var googleSignInClient: GoogleSignInClient
  /**
   * Signs in a user using a Google ID token.
   *
   * @param idToken The Google ID token for the user.
   * @return `true` if the sign-in operation is successful, otherwise `false`.
   * @throws Exception if the sign-in operation fails.
   */
  override suspend fun signInWithGoogle(idToken: String): Boolean {
    val credential = credentialProvider(idToken, null)
    val authResult = firebaseAuth.signInWithCredential(credential).await()
    return authResult.user != null
  }
  /**
   * Signs out the current user.
   *
   * @return `true` indicating the sign-out operation was successful.
   */
  override suspend fun signOut(): Boolean {
    firebaseAuth.signOut()
    return true
  }
  /**
   * Retrieves the email address of the currently signed-in user.
   *
   * @return The email of the current user, or `null` if no user is signed in.
   */
  override fun getCurrentUser(): String? {
    return firebaseAuth.currentUser?.email
  }
  /**
   * Handles the result of a Google Sign-In activity.
   *
   * @param result The result of the Google Sign-In activity.
   * @param onAuthComplete Callback invoked when authentication is successful.
   * @param onAuthError Callback invoked when an authentication error occurs.
   */
  override fun handleAuthResult(
      result: ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  ) {
    if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
      val task = googleSignInHelper(result.data)
      try {
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken
        if (idToken != null) {
          val credential = credentialProvider(idToken, null)
          firebaseAuth
              .signInWithCredential(credential)
              .addOnSuccessListener { authResult -> onAuthComplete(authResult) }
              .addOnFailureListener { exception ->
                if (exception is ApiException) {
                  onAuthError(exception)
                } else {
                  Log.e("FirebaseAuthService", "Unexpected error", exception)
                }
              }
        } else {
          Log.e("FirebaseAuthService", "idToken is null")
          onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
        }
      } catch (e: ApiException) {
        Log.e("FirebaseAuthService", "Google Sign-In failed", e)
        onAuthError(e)
      }
    } else {
      Log.e("FirebaseAuthService", "Invalid ActivityResult")
      onAuthError(ApiException(Status.RESULT_INTERNAL_ERROR))
    }
  }
  /**
   * Returns an Intent for initiating the Google Sign-In process.
   *
   * @param context The context from which the Intent is requested.
   * @param token The web client ID token used for authentication.
   * @return The Intent for Google Sign-In.
   */
  override fun getSignInIntent(context: Context, token: String): Intent {
    if (!this::googleSignInClient.isInitialized) {
      initializeGoogleSignInClient(context, token)
    }
    return googleSignInClient.signInIntent
  }
  /**
   * Initializes the GoogleSignInClient with the given context and token.
   *
   * @param context The context used to configure the GoogleSignInClient.
   * @param token The web client ID token.
   */
  @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
  fun initializeGoogleSignInClient(context: Context, token: String) {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
    googleSignInClient = GoogleSignIn.getClient(context, gso)
  }
  /**
   * Indicates whether this implementation of `AuthService` is mocked.
   *
   * @return `false` for `FirebaseAuthService`, as it represents a real implementation.
   */
  override fun isMocked(): Boolean = false
  /**
   * Wrapper class for activity result data.
   *
   * @property resultCode The result code of the activity.
   * @property data The Intent data returned from the activity.
   */
  class ActivityResultWrapper(val resultCode: Int, val data: Intent?)
}
