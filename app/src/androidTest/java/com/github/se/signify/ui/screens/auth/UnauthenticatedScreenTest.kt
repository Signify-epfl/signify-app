package com.github.se.signify.ui.screens.auth

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class UnauthenticatedScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Test
  fun allElementsAreCorrectlyDisplayed() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent { UnauthenticatedScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("UnauthenticatedScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UnauthenticatedText").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("logInButton").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun loginButtonRoutesToLoginScreen() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent { UnauthenticatedScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("logInButton").performClick()
    verify(navigationActions).navigateTo(Screen.AUTH)
  }
}
