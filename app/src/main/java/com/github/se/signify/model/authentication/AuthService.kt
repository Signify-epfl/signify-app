package com.github.se.signify.model.authentication

import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult

/**
 * `AuthService` defines the contract for authentication services in the application. It provides
 * methods for handling authentication logic, including signing in, signing out, retrieving the
 * current user, and managing authentication results.
 *
 * This interface allows for flexible implementation, supporting both real (e.g.,
 * FirebaseAuthService) and mocked authentication services (e.g., MockAuthService) for testing
 * purposes.
 */
interface AuthService {
  /**
   * Signs in the user with a Google ID token.
   *
   * @param idToken The Google ID token for the user.
   * @return A boolean indicating the success of the sign-in operation.
   */
  suspend fun signInWithGoogle(idToken: String): Boolean
  /**
   * Signs out the current user.
   *
   * @return A boolean indicating the success of the sign-out operation.
   */
  suspend fun signOut(): Boolean
  /**
   * Retrieves the current signed-in user's identifier.
   *
   * @return The identifier of the current user, or `null` if no user is signed in.
   */
  fun getCurrentUser(): String?
  /**
   * Handles the result of a sign-in activity.
   *
   * @param result The result wrapper containing the result code and data.
   * @param onAuthComplete A callback invoked upon successful authentication.
   * @param onAuthError A callback invoked when an authentication error occurs.
   */
  fun handleAuthResult(
      result: FirebaseAuthService.ActivityResultWrapper,
      onAuthComplete: (AuthResult) -> Unit,
      onAuthError: (ApiException) -> Unit
  )
  /**
   * Retrieves an intent for initiating the Google sign-in process.
   *
   * @param context The context from which the intent is requested.
   * @param token The web client ID token used for authentication.
   * @return An `Intent` for starting the sign-in process.
   */
  fun getSignInIntent(context: Context, token: String): Intent
  /**
   * Indicates whether this implementation of `AuthService` is a mocked version.
   *
   * @return `true` if the service is mocked; `false` otherwise.
   */
  fun isMocked(): Boolean
}
