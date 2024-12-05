package com.github.se.signify.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class WelcomeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun welcomeScreenDisplaysCorrectInitialImageAndText() {
    // Set the content of the WelcomeScreen
    composeTestRule.setContent { WelcomeScreen(navigationActions) }

    // Verify that the first image (letter_s) is displayed
    composeTestRule.onNodeWithContentDescription("Hand Sign Images").assertIsDisplayed()

    // Verify that the welcome text is displayed correctly
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
  }
}
