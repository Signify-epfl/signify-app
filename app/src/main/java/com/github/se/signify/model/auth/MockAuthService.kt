package com.github.se.signify.model.auth

class MockAuthService : AuthService {

  override suspend fun signInWithGoogle(idToken: String): Boolean {
    return true
  }

  override suspend fun signOut(): Boolean {
    return true
  }

  override fun getCurrentUser(): String {
    return "foo@example.com"
  }
}
