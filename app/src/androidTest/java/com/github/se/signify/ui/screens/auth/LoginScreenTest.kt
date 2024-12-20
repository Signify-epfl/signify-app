package com.github.se.signify.ui.screens.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.authentication.FirebaseAuthService
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
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

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = mock(UserSession::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.AUTH.route)
    Intents.init()
    composeTestRule.setContent { LoginScreen(navigationActions, {}, FirebaseAuthService()) }

    composeTestRule.waitForIdle()
  }

  @After
  fun tearDown() {
    Intents.release()
  }

  @Test
  fun googleSignInReturnsValidActivityResult() {

    composeTestRule.onNodeWithTag("loginButton").performClick()
    // assert that an Intent resolving to Google Mobile Services has been sent (for sign-in)
    intended(toPackage("com.google.android.gms"))
  }

  @Test
  fun offlineModeHelpsToConnect() {
    composeTestRule.onNodeWithTag("skipLoginButton").assertIsDisplayed().performClick()
  }
}
