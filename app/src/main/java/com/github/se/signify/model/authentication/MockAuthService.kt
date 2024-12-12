@file:Suppress("DEPRECATION")

package com.github.se.signify.model.authentication

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult

/**
 * `MockAuthService` is a mock implementation of the `AuthService` interface, designed for testing
 * purposes. It provides fake authentication logic to simulate user sign-in, sign-out, and other
 * authentication-related functionalities without requiring external services like Firebase.
 */
class MockAuthService : AuthService {

  /**
   * Simulates signing in a user with a Google ID token.
   *
   * @param idToken The Google ID token for the user.
   * @return Always returns `true` to indicate a successful sign-in.
   */
  override suspend fun signInWithGoogle(idToken: String): Boolean {
    return true
  }

  /**
   * Simulates signing out the current user.
   *
   * @return Always returns `true` to indicate a successful sign-out.
   */
  override suspend fun signOut(): Boolean {
    return true
  }

  /**
   * Simulates retrieving an Intent for initiating the Google sign-in process.
   *
   * @param context The context from which the Intent is requested.
   * @param token The web client ID token used for authentication.
   * @return A mocked Intent for the Google sign-in process.
   */
  override fun getSignInIntent(context: Context, token: String): Intent {
    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .setAccountName("mock-token")
            .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    return googleSignInClient.signInIntent
  }

  /**
   * Indicates that this is a mocked implementation of `AuthService`.
   *
   * @return Always returns `true`.
   */
  override fun isMocked(): Boolean = true

  /**
   * Simulates retrieving the current signed-in user's identifier.
   *
   * @return A fake email "mock-token@example.com" as the current user's identifier.
   */
  override fun getCurrentUser(): String {
    return "mock-token@example.com"
  }

  /**
   * Handles the result of a sign-in activity.
   *
   * This mock implementation does not perform any real actions, as it is intended for testing
   * purposes and does not use Firebase.
   *
   * @param result The result wrapper containing the result code and data.
   * @param onAuthComplete A callback invoked upon successful authentication.
   * @param onAuthError A callback invoked when an authentication error occurs.
   */
  override fun handleAuthResult(
      result: FirebaseAuthService.ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  ) {
    // No operation performed, as this is a mocked implementation.
  }
}
