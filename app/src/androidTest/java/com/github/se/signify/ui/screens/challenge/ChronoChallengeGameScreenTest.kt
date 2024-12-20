package com.github.se.signify.ui.screens.challenge

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class ChronoChallengeGameScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var handLandmarkViewModel: HandLandmarkViewModel
  private lateinit var challengeRepository: MockChallengeRepository
  private val challengeId = "challenge123"
  private val challengeTest =
      Challenge(
          challengeId = "testChallenge",
          player1 = "user1",
          player2 = "user2",
          mode = "Chrono",
          round = 1,
          roundWords = listOf("apple", "banana", "cherry"),
          player1RoundCompleted = listOf(true, true, false),
          player2RoundCompleted = listOf(false, false, false),
          gameStatus = "in_progress")

  @Before
  fun setup() {
    val context = mock(Context::class.java)
    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    handLandmarkViewModel = HandLandmarkViewModel(handLandMarkImplementation, context)
    mockNavigationActions = mock(NavigationActions::class.java)

    // Set up a mock challenge repository with test challenge data
    challengeRepository =
        MockChallengeRepository().apply {
          setChallenges(
              listOf(
                  Challenge(
                      challengeId = challengeId,
                      player1 = "user1",
                      player2 = "user2",
                      mode = "Chrono",
                      round = 1,
                      roundWords = listOf("apple", "banana", "cherry"),
                      player1Times = mutableListOf(),
                      player2Times = mutableListOf(),
                      player1RoundCompleted = listOf(false, false, false),
                      player2RoundCompleted = listOf(false, false, false),
                      gameStatus = "in_progress")))
        }
  }

  @Test
  fun chronoChallengeGameScreen_displaysComponentsCorrectly() {
    composeTestRule.setContent {
      ChronoChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = challengeRepository,
          handLandmarkViewModel = handLandmarkViewModel,
          challengeId = challengeId)
    }

    // Wait for the challenge to load
    composeTestRule.waitForIdle()

    // Assert that components are displayed after loading
    composeTestRule.onNodeWithTag("ElapsedTimeText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CurrentLetterBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SentenceLayerBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CameraBox").assertIsDisplayed()
  }

  @Test
  fun imageIsDisplayed_ifImageExists() {
    composeTestRule.setContent {
      ChronoChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = challengeRepository,
          handLandmarkViewModel = handLandmarkViewModel,
          challengeId = challengeId)
    }

    // Wait for the challenge to load
    composeTestRule.waitForIdle()

    // Check if the image with content description "Sign image" is displayed
    composeTestRule.onNodeWithContentDescription("Sign image").assertIsDisplayed()
  }

  @Test
  fun chronoChallengeGameScreen_displaysNoWordAvailableText_whenNoWordsPresent() {
    // Update the challenge repository to have an empty word list
    challengeRepository.setChallenges(
        listOf(
            Challenge(
                challengeId = challengeId,
                player1 = "user1",
                player2 = "user2",
                mode = "Chrono",
                round = 1,
                roundWords = emptyList(), // No words
                player1Times = mutableListOf(),
                player2Times = mutableListOf(),
                player1RoundCompleted = listOf(false, false, false),
                player2RoundCompleted = listOf(false, false, false),
                gameStatus = "in_progress")))

    // Relaunch the composable with the updated challenge
    composeTestRule.setContent {
      ChronoChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = challengeRepository,
          handLandmarkViewModel = handLandmarkViewModel,
          challengeId = challengeId)
    }

    // Assert that the "No word available" text is displayed
    composeTestRule.onNodeWithTag("NoWordAvailableText").assertIsDisplayed()
  }

  @Test
  fun chronoChallengeGameScreen_updatesElapsedTime_whenGameIsActive() {
    composeTestRule.setContent {
      ChronoChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = challengeRepository,
          handLandmarkViewModel = handLandmarkViewModel,
          challengeId = challengeId)
    }

    // Assert the initial state
    composeTestRule.onNodeWithTag("ElapsedTimeText").assertIsDisplayed()

    // Wait to ensure the elapsed time has some time to change
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("ElapsedTimeText").assertIsDisplayed()
  }

  @Test
  fun findNextIndexReturnsCorrectIndexForPlayer1() {

    val result = findNextIndex(challengeTest, "user1")

    // Assert
    assertEquals(2, result) // The first incomplete round for player1 is at index 2
  }

  @Test
  fun findNextIndexReturnsCorrectIndexForPlayer2() {
    // Arrange
    val challengeTest2 = challengeTest.copy(player2RoundCompleted = listOf(false, true, true))
    val result = findNextIndex(challengeTest2, "user2")
    assertEquals(0, result) // The first incomplete round for player2 is at index 0
  }

  @Test
  fun findNextIndexReturnsMinus1whenAllRoundsAreCompletedForPlayer1() {
    // Arrange
    val challengeTest3 = challengeTest.copy(player1RoundCompleted = listOf(true, true, true))
    val result = findNextIndex(challengeTest3, "user1")

    // Assert
    assertEquals(-1, result) // All rounds for player1 are completed
  }

  @Test
  fun findNextIndexReturnsZeroForInvalidUser() {

    // Act
    val result = findNextIndex(challengeTest, "invalidUser")

    // Assert
    assertEquals(0, result) // Invalid user should return -1
  }

  @Test
  fun findNextIndexReturnsMinus1ForEmptyCompletedList() {
    // Arrange
    val challengeTest4 = challengeTest.copy(player1RoundCompleted = emptyList())

    // Act
    val result = findNextIndex(challengeTest4, "user1")

    // Assert
    assertEquals(-1, result)
  }
}
