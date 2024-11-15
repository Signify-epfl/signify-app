package com.github.se.signify.ui.screens.challenge

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.currentUserId
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CreateAChallengeScreenTest {
  @get:Rule val composeTestRule = createComposeRule()
  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: ChallengeRepository
  private val friends = mutableStateListOf("Alice", "Bob", "Charlie")

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userRepository = mock(UserRepository::class.java)
    challengeRepository = mock(ChallengeRepository::class.java)

    // Mock getFriendsList to return friends list
    doAnswer { invocation ->
          val onSuccess = invocation.arguments[1] as (List<String>) -> Unit
          onSuccess(friends)
        }
        .whenever(userRepository)
        .getFriendsList(eq(currentUserId), any(), any())

    // Set up the Composable content
    composeTestRule.setContent {
      CreateAChallengeScreen(
          navigationActions = navigationActions,
          userRepository = userRepository,
          challengeRepository = challengeRepository)
    }

    composeTestRule.waitForIdle() // Wait for UI to be fully loaded
  }

  @Test
  fun testChallengeTitleIsDisplayed() {
    composeTestRule.onNodeWithTag("ChallengeTitle").assertIsDisplayed()
  }

  @Test
  fun testNoFriendsAvailableTextIsDisplayedWhenNoFriends() {
    // Clear friends list to simulate no friends
    friends.clear()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("NoFriendsText").assertIsDisplayed()
  }

  @Test
  fun testFriendsListIsDisplayed() {
    friends.forEachIndexed { index, friend ->
      composeTestRule.onNodeWithTag("FriendsList").performScrollToIndex(index)
      composeTestRule.waitForIdle()
      composeTestRule.onNodeWithTag("FriendCard_$friend").assertIsDisplayed()
      composeTestRule.onNodeWithText(friend).assertIsDisplayed()
    }
  }

  @Test
  fun testChallengeButtonIsDisplayedForEachFriend() {
    friends.forEach { friend ->
      composeTestRule.onNodeWithTag("ChallengeButton_$friend").assertIsDisplayed()
    }
  }

  @Test
  fun testChallengeDialogIsDisplayedWhenChallengeButtonClicked() {
    val friend = friends[0]
    composeTestRule.onNodeWithTag("ChallengeButton_$friend").performClick()
    composeTestRule.onNodeWithTag("DialogTitle").assertIsDisplayed()
  }
  /*
  EXPLANATION: The testUser initialized by the mock doesn't have an email,
  so its userId and UserName are set to "unknown" when accessing it from the repository
   */
  @Test
  fun testSelectChallengeModeAndSendChallenge() {
    val friend = friends[0]
    composeTestRule.onNodeWithTag("ChallengeButton_$friend").performClick()
    // Verify that the dialog is displayed
    composeTestRule.onNodeWithTag("DialogTitle").assertIsDisplayed()
    // Select "Sprint" mode
    composeTestRule.onNodeWithTag("SprintModeButton").performClick()
    composeTestRule.onNodeWithTag("SendChallengeButton").assertIsEnabled()
    composeTestRule.onNodeWithTag("SendChallengeButton").performClick()
    // Verify that sendChallengeRequest and addOngoingChallenge were called
    verify(challengeRepository)
        .sendChallengeRequest(eq(currentUserId), eq(friend), eq(ChallengeMode.SPRINT), any(), any(), any())
    verify(userRepository).addOngoingChallenge(eq(currentUserId), any(), any(), any())
    verify(userRepository).addOngoingChallenge(eq(friend), any(), any(), any())
  }

  @Test
  fun testCancelChallengeDialog() {
    val friend = friends[0]
    composeTestRule.onNodeWithTag("ChallengeButton_$friend").performClick()
    // Verify that the dialog is displayed
    composeTestRule.onNodeWithTag("DialogTitle").assertIsDisplayed()
    // Click "Cancel" button
    composeTestRule.onNodeWithTag("CancelButton").performClick()
    // Verify that the dialog is dismissed
    composeTestRule.onNodeWithTag("DialogTitle").assertDoesNotExist()
  }
}
