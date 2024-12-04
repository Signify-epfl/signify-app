package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ChallengeHistoryScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var userSession: MockUserSession
  private lateinit var navigationActions: NavigationActions
  private lateinit var statsRepository: StatsRepository

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = MockUserSession()
    statsRepository = mock(StatsRepository::class.java)
    composeTestRule.setContent {
      ChallengeHistoryScreen(navigationActions, userSession, statsRepository)
    }
  }

  @Test
  fun challengeHistoryScreenDisplaysCorrectElements() {

    // Verify top blue bar and back button is displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Verify challenges section is displayed
    composeTestRule.onNodeWithTag("ChallengesColumn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesRow").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesText").assertTextEquals("Number of challenges :")
    composeTestRule.onNodeWithTag("Completed").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Completed").assertTextEquals("Completed")
    composeTestRule.onNodeWithTag("Created").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Created").assertTextEquals("Created")
    composeTestRule.onNodeWithTag("Won").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Won").assertTextEquals("Won")

    // Verify graph placeholder is displayed
    composeTestRule.onNodeWithTag("GraphsAndStats").assertIsDisplayed()
  }

  @Test
  fun pressingBackArrowNavigatesToChallengeScreen() {

    composeTestRule.onNodeWithTag("BackButton").performClick()

    verify(navigationActions).goBack()
  }
}
