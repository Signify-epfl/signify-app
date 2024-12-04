package com.github.se.signify.model.auth

class MockUserSession(private val authService: AuthService = MockAuthService()) : UserSession {
  private var loggedIn = true

  override fun getUserId(): String? {
    return if (loggedIn) authService.getCurrentUser()?.split("@")?.get(0) else null
  }

  override suspend fun login(): Boolean {
    loggedIn = true
    return true
  }

  override suspend fun logout() {
    loggedIn = false
    authService.signOut()
  }

  override fun isLoggedIn(): Boolean {
    return authService.getCurrentUser() != null
  }
}
