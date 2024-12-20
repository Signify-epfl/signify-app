package com.github.se.signify.ui.screens.challenge

import android.Manifest
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.dependencyInjection.AppDependencyProvider
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class SprintChallengeGameScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  @get:Rule
  val cameraPermissionRule: GrantPermissionRule =
      GrantPermissionRule.grant(Manifest.permission.CAMERA)

  private lateinit var mockNavigationActions: NavigationActions
  private lateinit var mockChallengeRepository: MockChallengeRepository
  private lateinit var handLandmarkViewModel: HandLandmarkViewModel
  private lateinit var context: Context
  private val testChallengeId = "testChallengeId"
  private val currentUserId = "player1"
  private val testChallenge =
      Challenge(
          challengeId = testChallengeId,
          player1 = "player1",
          player2 = "player2",
          mode = "SPRINT",
          round = 5,
          roundWords = listOf("apple", "banana"),
          player1RoundCompleted = mutableListOf(false, false),
          player2RoundCompleted = mutableListOf(false, false),
          player1WordsCompleted = mutableListOf(1, 2),
          player2WordsCompleted = mutableListOf(2, 1),
          gameStatus = "in_progress")

  @Before
  fun setup() {

    mockNavigationActions = mock(NavigationActions::class.java)
    mockChallengeRepository = MockChallengeRepository()
    context = mock(Context::class.java)

    val handLandMarkImplementation = AppDependencyProvider.handLandMarkRepository()
    handLandmarkViewModel = HandLandmarkViewModel(handLandMarkImplementation, context)

    // Mock the repository to provide a test challenge
    mockChallengeRepository.setChallenges(
        listOf(
            Challenge(
                challengeId = testChallengeId,
                player1 = "user1",
                player2 = "user2",
                mode = "SPRINT",
                round = 1,
                roundWords = listOf("apple", "banana", "cherry"),
                player1RoundCompleted = mutableStateListOf(false, false, false),
                player2RoundCompleted = mutableStateListOf(false, false, false),
                player1WordsCompleted = mutableStateListOf(),
                player2WordsCompleted = mutableStateListOf(),
                gameStatus = "in_progress")))
  }

  @Test
  fun sprintChallengeGameScreen_displaysComponentsCorrectly() {
    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Assert that the main components are displayed
    composeTestRule.onNodeWithTag("TimerTextSprint").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WordDisplayBoxSprint").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CurrentGestureBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CompletedWordsCounterSprint").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CameraBoxSprint").assertIsDisplayed()
  }

  @Test
  fun sprintChallengeGameScreen_displaysLoadingText_whenChallengeIsNull() {
    // Update the repository to provide no challenges
    mockChallengeRepository.setChallenges(emptyList())

    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Assert that the loading text is displayed
    composeTestRule.onNodeWithTag("LoadingChallengeTextSprint").assertIsDisplayed()
  }

  @Test
  fun sprintChallengeGameScreen_updatesTimerCorrectly() {
    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Assert that the timer starts with the correct initial value
    composeTestRule.onNodeWithTag("TimerTextSprint").assertTextEquals("Time Left: 60 seconds")
  }

  @Test
  fun sprintChallengeGameScreen_updatesWordDisplayOnGestureRecognition() {
    // Mock the gesture detection to simulate user input
    whenever(handLandmarkViewModel.getSolution()).thenReturn("A")

    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Assert that the word display updates correctly
    composeTestRule.onNodeWithTag("WordDisplayBoxSprint").assertIsDisplayed()
  }

  @Test
  fun sprintChallengeGameScreen_displaysGestureIconCorrectly() {
    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Assert that the current gesture box is displayed with the correct icon
    composeTestRule.onNodeWithTag("CurrentGestureBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CurrentGestureIcon").assertIsDisplayed()
  }

  @Test
  fun sprintChallengeGameScreen_resetsWordAfterCompletionA() {
    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Simulate a word being completed
    whenever(handLandmarkViewModel.getSolution()).thenReturn("B")
    composeTestRule.waitForIdle()

    // Assert that the current word is reset after completion
    composeTestRule.onNodeWithTag("WordDisplayBoxSprint").assertIsDisplayed()
  }

  @Test
  fun sprintChallengeGameScreen_resetsWordAfterCompletion() {
    composeTestRule.setContent {
      SprintChallengeGameScreen(
          navigationActions = mockNavigationActions,
          userSession = MockUserSession(),
          challengeRepository = mockChallengeRepository,
          handLandMarkViewModel = handLandmarkViewModel,
          challengeId = testChallengeId)
    }

    composeTestRule.waitForIdle()

    // Simulate a word being completed
    whenever(handLandmarkViewModel.getSolution()).thenReturn("A")
    composeTestRule.waitForIdle()

    // Assert that the current word is reset after completion
    composeTestRule.onNodeWithTag("WordDisplayBoxSprint").assertIsDisplayed()
  }

  @Test
  fun updateSprintChallenge_updatesPlayer1RoundCompleted_whenPlayer1CompletesRound() {
    updateSprintChallenge(
        currentUserId, mockChallengeRepository, testChallenge, 3, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(testChallenge.challengeId)
    assert(updatedChallenge?.player1RoundCompleted?.contains(true) == true)
  }

  @Test
  fun updateSprintChallenge_updatesPlayer2RoundCompleted_whenPlayer2CompletesRound() {
    updateSprintChallenge(
        "player2", mockChallengeRepository, testChallenge, 2, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(testChallenge.challengeId)
    assert(updatedChallenge?.player2RoundCompleted?.contains(true) == true)
  }

  @Test
  fun updateSprintChallenge_addsWordsCompletedToPlayer1_whenPlayer1CompletesWord() {
    updateSprintChallenge(
        currentUserId, mockChallengeRepository, testChallenge, 5, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(testChallenge.challengeId)
    assert(updatedChallenge?.player1WordsCompleted?.contains(5) == true)
  }

  @Test
  fun updateSprintChallenge_addsWordsCompletedToPlayer2_whenPlayer2CompletesWord() {
    updateSprintChallenge(
        "player2", mockChallengeRepository, testChallenge, 4, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(testChallenge.challengeId)
    assert(updatedChallenge?.player2WordsCompleted?.contains(4) == true)
  }

  @Test
  fun updateSprintChallenge_setsGameStatusToCompleted_whenRoundIsSix() {
    val challengeAtRoundSix = testChallenge.copy(round = 6)
    updateSprintChallenge(
        currentUserId, mockChallengeRepository, challengeAtRoundSix, 3, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(challengeAtRoundSix.challengeId)
    assert(updatedChallenge?.gameStatus == "completed")
  }

  @Test
  fun updateSprintChallenge_setsGameStatusToInProgress_whenRoundIsNotSix() {
    updateSprintChallenge(
        currentUserId, mockChallengeRepository, testChallenge, 3, mockNavigationActions)

    val updatedChallenge = mockChallengeRepository.getChallenge(testChallenge.challengeId)
    assert(updatedChallenge?.gameStatus == "in_progress")
  }
}
