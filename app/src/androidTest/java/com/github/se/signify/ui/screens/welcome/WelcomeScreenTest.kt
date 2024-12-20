package com.github.se.signify.ui.screens.welcome

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull

class WelcomeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession

  @Before
  fun setUp() {
    navigationActions = Mockito.mock(NavigationActions::class.java)
    userSession = MockUserSession()
  }

  @Test
  fun welcomeScreenDisplaysCorrectInitialImageAndText() {
    // Set the content of the WelcomeScreen
    composeTestRule.setContent { WelcomeScreen(navigationActions, userSession) }

    // Verify that the first image (letter_s) is displayed
    composeTestRule.onNodeWithContentDescription("Hand Sign Images").assertIsDisplayed()

    // Verify that the welcome text is displayed correctly
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
  }

  @Test
  fun welcomeScreenRoutesToHomeScreen() {
    // Set the content of the WelcomeScreen
    composeTestRule.setContent { WelcomeScreen(navigationActions, userSession) }

    composeTestRule.mainClock.advanceTimeBy(welcomeScreenDuration())
    composeTestRule.waitForIdle()
    Mockito.verify(navigationActions).navigateTo(any(), anyOrNull())
  }

  @Test
  fun welcomeScreenIsSkippable() {
    // Set the content of the WelcomeScreen
    composeTestRule.setContent { WelcomeScreen(navigationActions, userSession) }
    composeTestRule.waitForIdle()
    repeat(welcomeScreenTitleLength()) {
      composeTestRule.onNodeWithTag("WelcomeScreen").performClick()
    }
    Mockito.verify(navigationActions).navigateTo(any(), anyOrNull())
  }
}
