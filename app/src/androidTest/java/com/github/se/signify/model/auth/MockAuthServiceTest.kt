package com.github.se.signify.model.auth

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import com.github.se.signify.model.authentication.MockAuthService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MockAuthServiceTest {
  private lateinit var authService: MockAuthService

  @Before
  fun setUp() {
    authService = MockAuthService()
  }

  @Test
  fun signInWithGoogleReturnsTrue() = runBlocking {
    authService = MockAuthService()
    val result = authService.signInWithGoogle("mockToken")
    assertTrue("signInWithGoogle should return true for mock token", result)
  }

  @Test
  fun getSignInIntentReturnNull() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val expectedToken = "mock-token"
    val resultIntent: Intent = authService.getSignInIntent(context, expectedToken)

    // Assert
    assertEquals(
        "Intent should match the configuration in MockAuthService",
        null,
        resultIntent.extras?.getString("accountName"))
  }

  @Test
  fun isMockedReturnsTrue() {
    assertTrue("The authService should be mocked", authService.isMocked())
  }

  @Test
  fun getCurrentUserReturnsActualUser() {
    assertEquals(
        "The MockAuthService should return the current mocked user",
        "mock-token@example.com",
        authService.getCurrentUser())
  }

  @Test
  fun mockAuthSignOut() = runBlocking {
    assertTrue("mockAuthSignOut should return true", authService.signOut())
  }
}
