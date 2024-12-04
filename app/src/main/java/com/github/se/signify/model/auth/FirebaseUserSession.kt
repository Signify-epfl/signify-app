package com.github.se.signify.model.auth

class FirebaseUserSession(private val authService: AuthService) : UserSession {
  override fun getUserId(): String {
    return authService.getCurrentUser()?.split("@")?.get(0) ?: "unknown"
  }

  override suspend fun login(): Boolean {
    return true
  }

  override suspend fun logout() {
    authService.signOut()
  }

  override fun isLoggedIn(): Boolean {
    return authService.getCurrentUser() != null
  }
}
