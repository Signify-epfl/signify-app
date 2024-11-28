package com.github.se.signify.ui.screens.challenge

// import androidx.compose.ui.test.*
// import com.github.se.signify.ui.navigation.Screen
// import org.junit.Assert.assertEquals
// import org.junit.Assert.assertTrue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class NewChallengeScreenTest {
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
              mode = "Sprint",
              status = "pending"),
          Challenge(
              challengeId = "challenge2",
              player1 = "testUserId",
              player2 = "Opponent2",
              mode = "Chrono",
              status = "active"))

  @Before
  fun setUp() {
    navigationActions = Mockito.mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = Mockito.mock(UserRepository::class.java)
    challengeRepository = MockChallengeRepository()

    // Mock UserRepository
    doAnswer { invocation ->
          val onSuccess = invocation.arguments[1] as (List<Challenge>) -> Unit
          onSuccess(ongoingChallenges)
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(userSession.getUserId()!!), any(), any())

    challengeRepository.setChallenges(ongoingChallenges)

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
  /**
   * @Test fun testTopBlueBarIsDisplayed() {
   *   composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed() }
   * @Test fun testBackButtonNavigatesBack() {
   *   composeTestRule.onNodeWithTag("BackButton").performClick() verify(navigationActions).goBack()
   *   }
   * @Test fun testMyFriendsButtonIsDisplayedAndNavigates() {
   *   composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed()
   *   composeTestRule.onNodeWithTag("MyFriendsButton").performClick()
   *   verify(navigationActions).navigateTo(Screen.FRIENDS) }
   * @Test fun testCreateChallengeButtonIsDisplayedAndNavigates() {
   *   composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed()
   *   composeTestRule.onNodeWithTag("CreateChallengeButton").performClick()
   *   verify(navigationActions).navigateTo(Screen.CREATE_CHALLENGE) } /**
   * @Test fun testOngoingChallengesDisplayed() {
   *   composeTestRule.onNodeWithTag("OngoingChallengesTitle").assertIsDisplayed()
   *   ongoingChallenges.forEachIndexed { index, challenge ->
   *   composeTestRule.onNodeWithTag("OngoingChallengeCard$index").assertIsDisplayed()
   *   composeTestRule.onNodeWithText("Opponent: ${challenge.player2}").assertIsDisplayed()
   *   composeTestRule.onNodeWithText("Mode: ${challenge.mode}").assertIsDisplayed() } } */
   * @Test fun testDeleteOngoingChallenge() { val challengeIdToDelete =
   *   ongoingChallenges[0].challengeId
   *
   * composeTestRule.onNodeWithTag("DeleteButton$challengeIdToDelete").assertExists()
   * composeTestRule.onNodeWithTag("DeleteButton$challengeIdToDelete").performClick()
   *
   * // Verify that the MockChallengeRepository is updated
   * assertTrue(challengeRepository.wasDeleteChallengeCalled()) assertEquals(challengeIdToDelete,
   * challengeRepository.lastDeletedChallengeId()) }
   */
  @Test
  fun testNewChallengeScreenViewModelInitialization() {
    verify(userRepository).getFriendsList(eq(userSession.getUserId()!!), any(), any())
    verify(userRepository).getOngoingChallenges(eq(userSession.getUserId()!!), any(), any())
  }
}
