package com.github.se.signify.model.authentication

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FirebaseUserSessionTest {
  private lateinit var firebaseUserSessionTest: FirebaseUserSession

  @Before
  fun setUp() {
    firebaseUserSessionTest = FirebaseUserSession(MockAuthService())
  }

  @Test
  fun getUserIdTest() {
    assertEquals(
        "The MockAuthService should return the current mocked user",
        "mock-token",
        firebaseUserSessionTest.getUserId())
  }

  @Test fun loginTest() = runBlocking { assertTrue(firebaseUserSessionTest.login()) }

  @Test fun isLoggedInTest() = runBlocking { assertTrue(firebaseUserSessionTest.isLoggedIn()) }
}
