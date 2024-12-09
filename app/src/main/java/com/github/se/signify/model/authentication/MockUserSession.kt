package com.github.se.signify.model.authentication

class MockUserSession : UserSession {
  var loggedIn = true
  private var userId: String = "mockUserId"

  override fun getUserId(): String? {
    return if (loggedIn) userId else null
  }

  override suspend fun login(): Boolean {
    loggedIn = true
    return true
  }

  override suspend fun logout() {
    loggedIn = false
  }

  override fun isLoggedIn(): Boolean {
    return loggedIn
  }
}
