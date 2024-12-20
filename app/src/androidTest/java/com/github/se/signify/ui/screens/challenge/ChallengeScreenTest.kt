package com.github.se.signify.ui.screens.challenge

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeId
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ChallengeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var userSession: UserSession
  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: MockChallengeRepository

  private val ongoingChallenges =
      mutableStateListOf(
          Challenge(
              challengeId = "challenge1",
              player1 = "testUserId",
              player2 = "Opponent1",
              mode = "Sprint"),
          Challenge(
              challengeId = "challenge2",
              player1 = "testUserId",
              player2 = "Opponent2",
              mode = "Chrono"))

  @Before
  fun setUp() {
    navigationActions = Mockito.mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = Mockito.mock(UserRepository::class.java)
    challengeRepository = MockChallengeRepository()

    // Mock UserRepository to provide ongoing challenges list
    doAnswer { invocation ->
          val onSuccess = invocation.getArgument<(List<ChallengeId>) -> Unit>(1)
          onSuccess.invoke(ongoingChallenges.map { it.challengeId })
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(userSession.getUserId()!!), any(), any())

    challengeRepository.setChallenges(ongoingChallenges)

    // Set up the Composable content

    composeTestRule.waitForIdle()
  }

  @Test
  fun challengeScreenDisplaysAllElements() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    composeTestRule.onNodeWithTag("ChallengeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("OngoingChallengesBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("OngoingChallengesTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("OngoingChallengesLazyColumn").assertIsDisplayed()
  }

  @Test
  fun testMyFriendsButton_click_navigatesToFriendsScreen() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed().performClick()

    verify(navigationActions).navigateTo(Screen.FRIENDS)
  }

  @Test
  fun challengeHistoryButtonNavigatesToChallengeHistoryScreen() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    composeTestRule.onNodeWithTag("ChallengeHistoryButton").assertIsDisplayed().performClick()
    verify(navigationActions).navigateTo(Screen.CHALLENGE_HISTORY)
  }

  @Test
  fun testCreateChallengeButton_click_navigatesToCreateChallengeScreen() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed().performClick()

    verify(navigationActions).navigateTo(Screen.CREATE_CHALLENGE)
  }

  @Test
  fun testOngoingChallengesLazyColumn_displaysAllChallenges() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    composeTestRule.onNodeWithTag("OngoingChallengesLazyColumn").assertIsDisplayed()

    ongoingChallenges.forEachIndexed { index, _ ->
      val challengeCardTag = "OngoingChallengeCard$index"
      composeTestRule.onNodeWithTag(challengeCardTag).assertIsDisplayed()
    }
  }

  @Test
  fun testDeleteButton_click() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    val challengeId = ongoingChallenges[0].challengeId

    // Click the delete button for the first ongoing challenge
    composeTestRule.onNodeWithTag("DeleteButton$challengeId").assertIsDisplayed().performClick()
  }

  @Test
  fun testDeleteButton_showsConfirmationDialog() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    val challengeId = ongoingChallenges[0].challengeId

    // Click the delete button for the first ongoing challenge
    composeTestRule.onNodeWithTag("DeleteButton$challengeId").performClick()

    // Check if the confirmation dialog is displayed
    composeTestRule.onNodeWithTag("ConfirmationDialog").assertIsDisplayed()
  }

  @Test
  fun testConfirmationDialog_buttonsFunctionality() {
    composeTestRule.setContent {
      ChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }
    val challengeId = ongoingChallenges[0].challengeId
    composeTestRule.onNodeWithTag("DeleteButton$challengeId").performClick()

    // Verify the "No" button is displayed and click it
    composeTestRule.onNodeWithText("No").assertIsDisplayed().performClick()

    // Ensure the dialog is dismissed
    composeTestRule.onNodeWithTag("ConfirmationDialog").assertDoesNotExist()

    // Click delete and verify "Yes" button dismisses the dialog
    composeTestRule.onNodeWithTag("DeleteButton$challengeId").performClick()
    composeTestRule.onNodeWithText("Yes").assertIsDisplayed().performClick()

    // Verify the dialog is dismissed
    composeTestRule.onNodeWithTag("ConfirmationDialog").assertDoesNotExist()
  }

  @Test
  fun onGoingChallengeCard_displaysCorrectDetailsSprint() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1 = "testUserId",
            player2 = "opponent1",
            player1WordsCompleted = mutableListOf(1, 2, 2),
            player2WordsCompleted = mutableListOf(1, 1, 2),
            player1RoundCompleted = listOf(false, false, false),
            player2RoundCompleted = listOf(false, false, false),
            mode = ChallengeMode.SPRINT.toString(),
            winner = "testUserId")

    composeTestRule.setContent {
      OngoingChallengeCard(challenge = challenge, {}, {}, userSession = userSession)
    }

    // Verify the challenge details are displayed correctly
    composeTestRule.onNodeWithText("Opponent: testUserId").assertIsDisplayed()

    composeTestRule.onNodeWithText("Mode: ${ChallengeMode.SPRINT}").assertIsDisplayed()
    composeTestRule.onNodeWithText("Round: 0/3").assertIsDisplayed()
  }

  @Test
  fun onGoingChallengeCard_displaysCorrectDetailsChrono() {
    val challenge =
        Challenge(
            challengeId = "challenge1",
            player1 = "testUserId",
            player2 = "opponent1",
            player1WordsCompleted = mutableListOf(1, 2, 2),
            player2WordsCompleted = mutableListOf(1, 1, 2),
            player1RoundCompleted = listOf(false, false, false),
            player2RoundCompleted = listOf(false, false, false),
            mode = ChallengeMode.CHRONO.toString(),
            winner = "testUserId")

    composeTestRule.setContent {
      OngoingChallengeCard(challenge = challenge, {}, {}, userSession = userSession)
    }

    // Verify the challenge details are displayed correctly
    composeTestRule.onNodeWithText("Opponent: testUserId").assertIsDisplayed()

    composeTestRule.onNodeWithText("Mode: ${ChallengeMode.CHRONO}").assertIsDisplayed()
    composeTestRule.onNodeWithText("Round: 0/3").assertIsDisplayed()
  }

    @Test
    fun challengeScreen_displaysInfoButton() {
        composeTestRule.setContent {
            ChallengeScreen(
                navigationActions = navigationActions,
                userSession = userSession,
                userRepository = userRepository,
                challengeRepository = challengeRepository)
        }

        // Assert that the Info button is displayed
        composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
        composeTestRule.onNodeWithTag("InfoButton").assertTextEquals("Info")
    }

    @Test
    fun challengeScreen_displaysInfoDialog_whenInfoButtonIsClicked() {
        composeTestRule.setContent {
            ChallengeScreen(
                navigationActions = navigationActions,
                userSession = userSession,
                userRepository = userRepository,
                challengeRepository = challengeRepository)
        }

        // Click the Info button
        composeTestRule.onNodeWithTag("InfoButton").performClick()

        // Assert that the Info dialog is displayed
        composeTestRule.onNodeWithTag("ChallengeInfoDialog").assertIsDisplayed()

        // Assert that the dialog contains the correct text
        composeTestRule.onNodeWithText("How Challenges Work").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sprint Mode: Best-of-3, type the maximum words in 1 minute.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Chrono Mode: Best-of-3, type a word as fast as possible each round.")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Results are displayed in the History screen (top-left button) when both players finished their round.")
            .assertIsDisplayed()
    }



}
