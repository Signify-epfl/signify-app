package com.github.se.signify.model.authentication

/**
 * `MockUserSession` is a mock implementation of the `UserSession` interface, designed for testing
 * purposes. It provides simulated session management functionality, including login, logout, and
 * user identification, without relying on real authentication mechanisms.
 *
 * @property authService An instance of `AuthService` used for authentication operations. Defaults
 *   to `MockAuthService`.
 */
class MockUserSession(private val authService: AuthService = MockAuthService()) : UserSession {

  /** Tracks the login state of the user. */
  var loggedIn = true

  /**
   * Retrieves the unique user ID based on the user's email address.
   *
   * The ID is derived by extracting the portion of the email address before the "@" symbol. If the
   * user is not logged in, `null` is returned.
   *
   * @return The user ID or `null` if the user is not logged in.
   */
  override fun getUserId(): String? {
    return if (loggedIn) authService.getCurrentUser()?.split("@")?.get(0) else null
  }

  /**
   * Simulates the login process for the user.
   *
   * @return The current login state (`true` if logged in, `false` otherwise).
   */
  override suspend fun login(): Boolean {
    return loggedIn
  }

  /**
   * Simulates the logout process for the user.
   *
   * Sets `loggedIn` to `false` and invokes the `signOut` method of the `AuthService`.
   */
  override suspend fun logout() {
    loggedIn = false
    authService.signOut()
  }

  /**
   * Checks whether the user is currently logged in.
   *
   * @return `true` if the user is logged in; `false` otherwise.
   */
  override fun isLoggedIn(): Boolean {
    return loggedIn
  }
}
