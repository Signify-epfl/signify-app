package com.github.se.signify.model.authentication

/**
 * `FirebaseUserSession` is an implementation of the `UserSession` interface that manages the user's
 * session using an `AuthService`.
 *
 * This class acts as a wrapper around the `AuthService` to provide functionalities such as
 * retrieving the user ID, checking login status, and handling login/logout operations.
 *
 * @property authService The `AuthService` instance used for authentication operations.
 */
class FirebaseUserSession(private val authService: AuthService) : UserSession {

  /**
   * Retrieves the unique user ID based on the user's email address.
   *
   * The ID is derived by extracting the portion of the email address before the "@" symbol. If no
   * user is logged in, "unknown" is returned.
   *
   * @return The user ID or "unknown" if no user is logged in.
   */
  override fun getUserId(): String {
    return authService.getCurrentUser()?.split("@")?.get(0) ?: "unknown"
  }

  /**
   * Handles the login process for the user.
   *
   * Note: In this implementation, the login always returns `true`, as the actual login logic is
   * managed externally (e.g., via the `AuthService`).
   *
   * @return `true`, indicating the login process was initiated successfully.
   */
  override suspend fun login(): Boolean {
    return true
  }

  /** Logs out the current user by delegating to the `AuthService`. */
  override suspend fun logout() {
    authService.signOut()
  }

  /**
   * Checks whether the user is currently logged in.
   *
   * @return `true` if a user is logged in; `false` otherwise.
   */
  override fun isLoggedIn(): Boolean {
    return authService.getCurrentUser() != null
  }
}
