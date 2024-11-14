package com.github.se.signify.ui.screens.challenge

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.user.User
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.currentUserId
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
  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: ChallengeRepository
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
  private val opponentUser =
      User(uid = "Opponent1", name = "Opponent User", ongoingChallenges = listOf("challenge1"))

  @Before
  fun setUp() {
    navigationActions = Mockito.mock(NavigationActions::class.java)
    userRepository = Mockito.mock(UserRepository::class.java)
    challengeRepository = Mockito.mock(ChallengeRepository::class.java)

    // Mocks
    doAnswer { invocation ->
          val onSuccess = invocation.arguments[1] as (List<Challenge>) -> Unit
          onSuccess(ongoingChallenges)
        }
        .whenever(userRepository)
        .getOngoingChallenges(eq(currentUserId), any(), any())

    doAnswer { invocation ->
          ongoingChallenges.removeIf { it.challengeId == invocation.arguments[1] }
          val onSuccess = invocation.arguments[2] as () -> Unit
          onSuccess()
        }
        .whenever(userRepository)
        .removeOngoingChallenge(any(), any(), any(), any())

    // Set up the Composable content and wait for idle state
    composeTestRule.setContent {
      NewChallengeScreen(
          navigationActions = navigationActions,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }

    composeTestRule.waitForIdle() // Ensure composable is idle before testing
  }

  // Step 2: Implement Basic UI Tests
  @Test
  fun testTopBlueBarIsDisplayed() {
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
  }

  @Test
  fun testBackButtonNavigatesBack() {
    // Click the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()
    // Verify navigation action is called
    verify(navigationActions).goBack()
  }

  @Test
  fun testMyFriendsButtonIsDisplayedAndNavigates() {
    // Check if "My Friends" button is displayed
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed()
    // Perform click action on the "My Friends" button
    composeTestRule.onNodeWithTag("MyFriendsButton").performClick()
    // Verify navigation action is called
    verify(navigationActions).navigateTo("Friends")
  }

  @Test
  fun testCreateChallengeButtonIsDisplayedAndNavigates() {
    // Check if "Create a Challenge" button is displayed
    composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed()
    // Perform click action on the "Create a Challenge" button
    composeTestRule.onNodeWithTag("CreateChallengeButton").performClick()
    // Verify navigation action is called
    verify(navigationActions).navigateTo("CreateChallenge")
  }
  // Step 3: Test Ongoing Challenges Display
  @Test
  fun testOngoingChallengesDisplayed() {
    // Verify the "My Ongoing Challenges" title is displayed
    composeTestRule.onNodeWithTag("OngoingChallengesTitle").assertIsDisplayed()
    // Scroll to each challenge card and verify it is displayed
    ongoingChallenges.forEachIndexed { index, challenge ->
      composeTestRule.onNodeWithTag("OngoingChallengeCard$index").assertIsDisplayed()
      composeTestRule.onNodeWithText("Opponent: ${challenge.player2}").assertIsDisplayed()
      composeTestRule.onNodeWithText("Mode: ${challenge.mode}").assertIsDisplayed()
    }
  }
  // Step 4: Test Deleting an Ongoing Challenge
  @Test
  fun testDeleteOngoingChallenge() {
    val challengeIdToDelete = ongoingChallenges[0].challengeId
    // Perform delete action on the challenge
    composeTestRule.onNodeWithTag("DeleteButton$challengeIdToDelete").assertExists()
    composeTestRule.onNodeWithTag("DeleteButton$challengeIdToDelete").performClick()
    // Verify removeOngoingChallenge and deleteChallenge were called
    /*
    EXPLANATION: The testUser initialized by the mock doesn't have an email,
    so its userId and UserName are set to "unknown" when accessing it from the repository
     */
    verify(userRepository)
        .removeOngoingChallenge(eq("unknown"), eq(challengeIdToDelete), any(), any())
    verify(userRepository)
        .removeOngoingChallenge(eq(opponentUser.uid), eq(challengeIdToDelete), any(), any())
    verify(challengeRepository).deleteChallenge(eq(challengeIdToDelete), any(), any())
    // Verify that ongoingChallenges list is updated after deletion
    composeTestRule.waitForIdle()
    assert(ongoingChallenges.size == 1) {
      "Ongoing challenges list should have one item after deletion"
    }
  }

  @Test
  fun testNewChallengeScreenViewModelInitialization() {
    // Verify that getFriendsList and getOngoingChallenges are called when the screen is displayed
    verify(userRepository).getFriendsList(eq(currentUserId), any(), any())
    verify(userRepository).getOngoingChallenges(eq(currentUserId), any(), any())
  }
}
