package com.github.se.signify.ui.screens.challenge

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.profile.stats.MockStatsRepository
import com.github.se.signify.model.profile.stats.StatsRepository
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
  private lateinit var statsRepository: StatsRepository

  private val challengesCompleted = 10
  private val challengesCreated = 5
  private val challengesWon = 3

  @Before
  fun setUp(): Unit = runBlocking {
    userSession = mock()
    userRepository = mock()
    challengeRepository = mock()
    navigationActions = mock()
      statsRepository = mock()

    whenever(userSession.getUserId()).thenReturn("testUserId")
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(
              challengesCompleted) // Trigger the onSuccess callback with the test value
        }
        .whenever(statsRepository)
        .getCompletedChallengeStats(eq("testUserId"), any(), any())

    // Mock the repository behavior for getChallengesCreated
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(challengesCreated) // Trigger the onSuccess callback with the test value
        }
        .whenever(statsRepository)
        .getCreatedChallengeStats(eq("testUserId"), any(), any())

    // Mock the repository behavior for getChallengesWon
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(Int) -> Unit>(1) // Capture the onSuccess callback
          onSuccess.invoke(challengesWon) // Trigger the onSuccess callback with the test value
        }
        .whenever(statsRepository)
        .getWonChallengeStats(eq("testUserId"), any(), any())
  }

  @Test
  fun challengeHistoryScreen_displaysStatisticsCorrectly(): Unit = runBlocking {
    composeTestRule.setContent {
      ChallengeHistoryScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository,
          statsRepository = statsRepository
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
          statsRepository = statsRepository
      )
    }

    // Verify the message for no past challenges is displayed
    composeTestRule.onNodeWithTag("NoPastChallengesText").assertIsDisplayed()
  }

  @Test
  fun pastChallengeCard_displaysCorrectDetailsChrono() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1 = "testUserId",
            player2 = "opponent1",
            player1Times = mutableListOf(1000L, 2000L),
            player2Times = mutableListOf(1500L, 2500L),
            mode = ChallengeMode.CHRONO.toString(),
            winner = "testUserId")

    composeTestRule.setContent {
      PastChallengeCard(challenge = challenge, userSession = userSession)
    }

    // Verify the challenge details are displayed correctly
    composeTestRule.onNodeWithText("Opponent: opponent1").assertIsDisplayed()
    composeTestRule.onNodeWithText("Mode: ${ChallengeMode.CHRONO}").assertIsDisplayed()
    composeTestRule.onNodeWithText("testUserId Score: 3.0 s").assertIsDisplayed()
    composeTestRule.onNodeWithText("opponent1 Score: 4.0 s").assertIsDisplayed()
    composeTestRule.onNodeWithText("Winner: testUserId").assertIsDisplayed()
  }

  @Test
  fun pastChallengeCard_displaysCorrectDetailsSprint() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1 = "testUserId",
            player2 = "opponent1",
            player1WordsCompleted = mutableListOf(1, 2, 2),
            player2WordsCompleted = mutableListOf(1, 1, 2),
            mode = ChallengeMode.SPRINT.toString(),
            winner = "testUserId")

    composeTestRule.setContent {
      PastChallengeCard(challenge = challenge, userSession = userSession)
    }

    // Verify the challenge details are displayed correctly
    composeTestRule.onNodeWithText("Opponent: opponent1").assertIsDisplayed()
    composeTestRule.onNodeWithText("Mode: ${ChallengeMode.SPRINT}").assertIsDisplayed()
    composeTestRule.onNodeWithText("testUserId Score: 5.0 words").assertIsDisplayed()
    composeTestRule.onNodeWithText("opponent1 Score: 4.0 words").assertIsDisplayed()
    composeTestRule.onNodeWithText("Winner: testUserId").assertIsDisplayed()
  }

  @Test
  fun calculatePlayerResult_returnsCorrectResultForSprintMode() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1WordsCompleted = mutableListOf(3, 2, 1),
            player2WordsCompleted = mutableListOf(4, 3, 2),
            mode = ChallengeMode.SPRINT.toString())

    val player1Result = calculatePlayerResult(challenge, isPlayer1 = true)
    val player2Result = calculatePlayerResult(challenge, isPlayer1 = false)

    assert(player1Result == 6.0) // Sum of 3, 2, 1
    assert(player2Result == 9.0) // Sum of 4, 3, 2
  }

  @Test
  fun calculatePlayerResult_returnsCorrectResultForChronoMode() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1Times = mutableListOf(1000L, 2000L, 3000L),
            player2Times = mutableListOf(1500L, 2500L, 3500L),
            mode = ChallengeMode.CHRONO.toString())

    val player1Result = calculatePlayerResult(challenge, isPlayer1 = true)
    val player2Result = calculatePlayerResult(challenge, isPlayer1 = false)

    assert(player1Result == 6.0) // Sum of times in seconds: 1000+2000+3000 = 6.0 seconds
    assert(player2Result == 7.5) // Sum of times in seconds: 1500+2500+3500 = 7.5 seconds
  }

  // Test `determineWinner`
  @Test
  fun determineWinner_returnsCorrectWinnerForSprintMode() {
    val winner =
        determineWinner(
            mode = ChallengeMode.SPRINT.toString(),
            player1 = "player1",
            player2 = "player2",
            player1Result = 15.0,
            player2Result = 10.0)

    assert(winner == "player1") // Higher score wins
  }

  @Test
  fun determineWinner_returnsDrawForSprintMode() {
    val winner =
        determineWinner(
            mode = ChallengeMode.SPRINT.toString(),
            player1 = "player1",
            player2 = "player2",
            player1Result = 12.0,
            player2Result = 12.0)

    assert(winner == "Draw") // Same score
  }

  @Test
  fun determineWinner_returnsCorrectWinnerForChronoMode() {
    val winner =
        determineWinner(
            mode = ChallengeMode.CHRONO.toString(),
            player1 = "player1",
            player2 = "player2",
            player1Result = 5.0,
            player2Result = 10.0)

    assert(winner == "player1") // Lower time wins
  }

  @Test
  fun determineWinner_returnsDrawForChronoMode() {
    val winner =
        determineWinner(
            mode = ChallengeMode.CHRONO.toString(),
            player1 = "player1",
            player2 = "player2",
            player1Result = 7.0,
            player2Result = 7.0)

    assert(winner == "Draw") // Same time
  }

  // Test `PlayerScoreText`
  @Test
  fun playerScoreText_displaysCorrectScoreForSprintMode() {
    composeTestRule.setContent {
      PlayerScoreText(player = "player1", result = 12.0, mode = ChallengeMode.SPRINT.toString())
    }

    composeTestRule.onNodeWithText("player1 Score: 12.0 words").assertIsDisplayed()
  }

  @Test
  fun playerScoreText_displaysCorrectScoreForChronoMode() {
    composeTestRule.setContent {
      PlayerScoreText(player = "player1", result = 5.5, mode = ChallengeMode.CHRONO.toString())
    }

    composeTestRule.onNodeWithText("player1 Score: 5.5 s").assertIsDisplayed()
  }
}
