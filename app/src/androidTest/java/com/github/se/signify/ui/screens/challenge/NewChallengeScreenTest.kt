package com.github.se.signify.ui.screens.challenge

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.authentication.MockUserSession
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
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

class NewChallengeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var userSession: UserSession
  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: MockChallengeRepository
  private lateinit var userViewModel: UserViewModel

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
              mode = "Chrono"),
          Challenge(
              challengeId = "challenge3",
              player1 = "testUserId",
              player2 = "Opponent1",
              mode = "Sprint",
              player1Times = mutableListOf(1000L, 2000L, 1500L), // Total: 4500
              player2Times = mutableListOf(1200L, 1800L, 1400L), // Total: 4400
              player1RoundCompleted = listOf(true, true, true),
              player2RoundCompleted = listOf(true, true, true),
              roundWords = listOf("word1", "word2", "word3")))

  @Before
  fun setUp() {
    navigationActions = Mockito.mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = Mockito.mock(UserRepository::class.java)
    challengeRepository = MockChallengeRepository()

    // Mock UserRepository to provide ongoing challenges list
    doAnswer { invocation ->
          val onSuccess = invocation.arguments[1] as (List<Challenge>) -> Unit
          onSuccess(ongoingChallenges)
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(userSession.getUserId()!!), any(), any())

    challengeRepository.setChallenges(ongoingChallenges)
    userViewModel = UserViewModel(userSession, userRepository)

    // Set up the Composable content
    composeTestRule.setContent {
      NewChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }

    composeTestRule.waitForIdle()
  }

  @Test
  fun testMyFriendsButton_click_navigatesToFriendsScreen() {
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed().performClick()

    verify(navigationActions).navigateTo(Screen.FRIENDS)
  }

  @Test
  fun testCreateChallengeButton_click_navigatesToCreateChallengeScreen() {
    composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed().performClick()

    verify(navigationActions).navigateTo(Screen.CREATE_CHALLENGE)
  }

  @Test
  fun testOngoingChallengesBox_isDisplayed() {
    composeTestRule.onNodeWithTag("OngoingChallengesBox").assertIsDisplayed()
  }

  @Test
  fun testOngoingChallengesTitle_isDisplayed() {
    composeTestRule
        .onNodeWithTag("OngoingChallengesTitle")
        .assertIsDisplayed()
        .assertTextEquals("My Ongoing Challenges")
  }

  @Test
  fun testOngoingChallengesLazyColumn_displaysAllChallenges() {
    composeTestRule.onNodeWithTag("OngoingChallengesLazyColumn").assertIsDisplayed()

    ongoingChallenges.forEachIndexed { index, _ ->
      val challengeCardTag = "OngoingChallengeCard$index"
      composeTestRule.onNodeWithTag(challengeCardTag).assertIsDisplayed()
    }
  }

  @Test
  fun testDeleteButton_click() {
    val challengeId = ongoingChallenges[0].challengeId

    // Click the delete button for the first ongoing challenge
    composeTestRule.onNodeWithTag("DeleteButton$challengeId").assertIsDisplayed().performClick()
  }
}
