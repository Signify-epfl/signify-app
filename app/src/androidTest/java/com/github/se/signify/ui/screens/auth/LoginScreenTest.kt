package com.github.se.signify.ui.screens.auth

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.auth.FirebaseAuthService
import com.github.se.signify.model.auth.MockAuthService
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import junit.framework.AssertionFailedError
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class LoginScreenTest : TestCase() {
  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession
  private lateinit var authService: MockAuthService

  @Before
  fun setUp() {
    Intents.init()

    navigationActions = mock(NavigationActions::class.java)
    userSession = mock(UserSession::class.java)
    authService = MockAuthService()
    `when`(navigationActions.currentRoute()).thenReturn(Screen.AUTH.route)
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun titleAndButtonAreCorrectlyDisplayed() {

    composeTestRule.setContent { LoginScreen(navigationActions, authService) }

    composeTestRule.onNodeWithTag("IntroMessage").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("IntroMessage")
        .assertTextEquals(
            "Signify is what you need to communicate with deaf and hard of hearing people")

    composeTestRule.onNodeWithTag("loginButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("loginButton").assertHasClickAction()
  }

  @Test
  fun googleSignInReturnsValidActivityResult() {
    // Use a real FirebaseAuthService
    val firebaseAuthService = FirebaseAuthService()
    composeTestRule.setContent { LoginScreen(navigationActions, firebaseAuthService) }

    composeTestRule.onNodeWithTag("loginButton").performClick()
    composeTestRule.waitForIdle()
    // assert that an Intent resolving to Google Mobile Services has been sent (for sign-in)
    intended(toPackage("com.google.android.gms"))
  }

  @Test
  fun googleSignInWithMockAuthServiceDoesNotLaunchIntent() {
    // Use MockAuthService for testing

    composeTestRule.setContent { LoginScreen(navigationActions, authService) }

    composeTestRule.onNodeWithTag("loginButton").performClick()
    composeTestRule.waitForIdle()

    // Assert that no intent to Google Mobile Services has been sent
    assertThrows(AssertionFailedError::class.java) { intended(toPackage("com.google.android.gms")) }
  }

  @Test
  fun offlineModeHelpsToConnect() {
    composeTestRule.setContent { LoginScreen(navigationActions, authService) }
    composeTestRule.onNodeWithTag("skipLoginButton").assertIsDisplayed().performClick()
  }
}
