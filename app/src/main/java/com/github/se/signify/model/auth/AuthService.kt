package com.github.se.signify.model.auth

interface AuthService {
  suspend fun signInWithGoogle(idToken: String): Boolean

  suspend fun signOut(): Boolean

  fun getCurrentUser(): String?
}
