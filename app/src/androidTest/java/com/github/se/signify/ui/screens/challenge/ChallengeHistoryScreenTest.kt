package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.ui.navigation.NavigationActions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ChallengeHistoryScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      ChallengeHistoryScreen(
          navigationActions = navigationActions,
          friendsChallengesAchieved = 5,
          challengesCreated = 3)
    }
  }

  @Test
  fun challengeHistoryScreenDisplaysCorrectElements(): Unit = runBlocking {

    // Assert that the key elements are displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeHistoryContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengesRow").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengesText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengesCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengesCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesCreatedCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesCreatedRow").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesCreatedText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesCreatedCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("GraphsStatisticsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
  }

  @Test
  fun pressingBackArrowNavigatesToChallengeScreen() {

    composeTestRule.onNodeWithTag("BackButton").performClick()

    verify(navigationActions).goBack()
  }
}
