package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ChallengeHistoryScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var statsRepository: StatsRepository
  private lateinit var statsViewModel: StatsViewModel

  private val friendChallengesAchieved = 5
  private val challengesCreated = 3

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    statsRepository = mock(StatsRepository::class.java)
    statsViewModel = StatsViewModel(statsRepository)
    composeTestRule.setContent {
      ChallengeHistoryScreen(navigationActions, statsRepository, statsViewModel)
    }

    /*// Mock the getCompletedChallengeStats method to return the number of completed challenges
    doAnswer { invocation ->
      val onSuccess = invocation.arguments[1] as (Int) -> Unit
      onSuccess(friendChallengesAchieved)
    }
      .`when`(statsRepository)
      .getCompletedChallengeStats(any(), any(), any())

    // Mock the getCreatedChallengeStats method to return the number of created challenges
    doAnswer { invocation ->
      val onSuccess = invocation.arguments[1] as (Int) -> Unit
      onSuccess(challengesCreated)
    }
      .`when`(statsRepository)
      .getCreatedChallengeStats(any(), any(), any())*/
  }

  @Test
  fun challengeHistoryScreenDisplaysCorrectElements() {

    // Verify top blue bar and back button is displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Verify friends challenges achieved section is displayed with counts
    composeTestRule.onNodeWithTag("FriendsChallengesRow").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengesText").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("FriendsChallengesText")
        .assertTextEquals("Number of friends challenges achieved")
    composeTestRule.onNodeWithTag("FriendsChallengesCountBox").assertIsDisplayed()
    /*composeTestRule.onNodeWithTag("$friendChallengesAchieved").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("$friendChallengesAchieved")
        .assertTextEquals("$friendChallengesAchieved")*/

    // Verify challenges created section is displayed with counts
    composeTestRule.onNodeWithTag("ChallengesCreatedRow").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengesCreatedText").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("ChallengesCreatedText")
        .assertTextEquals("Number of challenges created")
    composeTestRule.onNodeWithTag("ChallengesCreatedCountBox").assertIsDisplayed()
    /*composeTestRule.onNodeWithTag("$challengesCreated").assertIsDisplayed()
    composeTestRule
      .onNodeWithTag("$challengesCreated")
      .assertTextEquals("$challengesCreated")*/

    // Verify graph placeholder is displayed
    composeTestRule.onNodeWithTag("GraphsAndStats").assertIsDisplayed()
  }

  @Test
  fun pressingBackArrowNavigatesToChallengeScreen() {

    composeTestRule.onNodeWithTag("BackButton").performClick()

    verify(navigationActions).goBack()
  }
}
