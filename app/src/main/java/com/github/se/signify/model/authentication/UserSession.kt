package com.github.se.signify.model.authentication

interface UserSession {
  fun getUserId(): String?

  suspend fun login(): Boolean

  suspend fun logout()

  fun isLoggedIn(): Boolean
}
