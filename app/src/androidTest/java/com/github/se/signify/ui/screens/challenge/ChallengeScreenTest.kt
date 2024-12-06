package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ChallengeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent { ChallengeScreen(navigationActions = navigationActions) }
  }

  @Test
  fun challengeScreenDisplaysCorrectElements() {

    composeTestRule.onNodeWithTag("ChallengeButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HistoryButton").assertIsDisplayed()
  }

  @Test
  fun pressingHistoryButtonNavigatesToChallengeHistoryScreen() {

    composeTestRule.onNodeWithTag("HistoryButton").performClick()

    verify(navigationActions).navigateTo(Screen.CHALLENGE_HISTORY)
  }

  @Test
  fun pressingChallengeButtonNavigatesToNewChallengeScreen() {

    composeTestRule.onNodeWithTag("ChallengeButton").performClick()

    verify(navigationActions).navigateTo(Screen.NEW_CHALLENGE)
  }
}
