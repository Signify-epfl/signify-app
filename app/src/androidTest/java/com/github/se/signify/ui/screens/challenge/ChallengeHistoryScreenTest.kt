package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.navigation.NavigationActions
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doAnswer
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ChallengeHistoryScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: ChallengeRepository
  private lateinit var navigationActions: NavigationActions

  private val challengesCompleted = 10
  private val challengesCreated = 5
  private val challengesWon = 3

  @Before
  fun setUp(): Unit = runBlocking {
    userSession = mock()
    userRepository = mock()
    challengeRepository = mock()
    navigationActions = mock()

    whenever(userSession.getUserId()).thenReturn("testUserId")
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(
              challengesCompleted) // Trigger the onSuccess callback with the test value
        }
        .whenever(userRepository)
        .getChallengesCompleted(eq("testUserId"), any(), any())

    // Mock the repository behavior for getChallengesCreated
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(challengesCreated) // Trigger the onSuccess callback with the test value
        }
        .whenever(userRepository)
        .getChallengesCreated(eq("testUserId"), any(), any())

    // Mock the repository behavior for getChallengesWon
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(challengesWon) // Trigger the onSuccess callback with the test value
        }
        .whenever(userRepository)
        .getChallengesWon(eq("testUserId"), any(), any())
  }

  @Test
  fun challengeHistoryScreen_displaysStatisticsCorrectly(): Unit = runBlocking {
    composeTestRule.setContent {
      ChallengeHistoryScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository,
      )
    }

    // Verify statistics are displayed correctly
    composeTestRule.onNodeWithTag("ChallengesText").assertIsDisplayed()
    composeTestRule.onNodeWithText("$challengesCompleted").assertIsDisplayed()
    composeTestRule.onNodeWithText("$challengesCreated").assertIsDisplayed()
    composeTestRule.onNodeWithText("$challengesWon").assertIsDisplayed()
  }

  @Test
  fun challengeHistoryScreen_displaysNoPastChallengesTextWhenEmpty(): Unit = runBlocking {
    composeTestRule.setContent {
      ChallengeHistoryScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository,
      )
    }

    // Verify the message for no past challenges is displayed
    composeTestRule.onNodeWithTag("NoPastChallengesText").assertIsDisplayed()
  }

  @Test
  fun pastChallengeCard_displaysCorrectDetails() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1 = "testUserId",
            player2 = "opponent1",
            player1Times = mutableListOf(1000L, 2000L),
            player2Times = mutableListOf(1500L, 2500L),
            mode = "Sprint",
            winner = "testUserId")

    composeTestRule.setContent {
      PastChallengeCard(challenge = challenge, userSession = userSession)
    }

    // Verify the challenge details are displayed correctly
    composeTestRule.onNodeWithText("Opponent: opponent1").assertIsDisplayed()
    composeTestRule.onNodeWithText("Mode: Sprint").assertIsDisplayed()
    composeTestRule.onNodeWithText("testUserId Score: 3 s").assertIsDisplayed()
    composeTestRule.onNodeWithText("opponent1 Score: 4 s").assertIsDisplayed()
    composeTestRule.onNodeWithText("Winner: testUserId").assertIsDisplayed()
  }
}
