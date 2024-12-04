package com.github.se.signify.model.di

import com.github.se.signify.model.auth.AuthService
import com.github.se.signify.model.auth.MockAuthService
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession

class MockDependencyProvider : AppDependencyProvider() {
  override fun provideAuthService(): AuthService {
    return MockAuthService()
  }

  override fun userSession(): UserSession {
    return MockUserSession()
  }
}
