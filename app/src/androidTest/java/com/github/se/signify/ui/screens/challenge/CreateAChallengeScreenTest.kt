package com.github.se.signify.ui.screens.challenge

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.MockChallengeRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

class CreateAChallengeScreenTest {
  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var userSession: UserSession
  private lateinit var navigationActions: NavigationActions
  private lateinit var userRepository: UserRepository
  private lateinit var challengeRepository: MockChallengeRepository
  private val friends = mutableStateListOf("Alice", "Bob", "Charlie")

  @Before
  fun setUp() {
    userSession = MockUserSession()
    navigationActions = mock(NavigationActions::class.java)
    userRepository = mock(UserRepository::class.java)
    challengeRepository = MockChallengeRepository()

    // Mock getFriendsList to return friends list
    doAnswer { invocation ->
          val onSuccess = invocation.arguments[1] as (List<String>) -> Unit
          onSuccess(friends)
        }
        .whenever(userRepository)
        .getFriendsList(eq(userSession.getUserId()!!), any(), any())

    // Set up the Composable content
    composeTestRule.setContent {
      CreateAChallengeScreen(
          navigationActions = navigationActions,
          userSession = userSession,
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

    // Verify that the challengeRepository was called
    assertTrue(challengeRepository.wasSendChallengeCalled())
    assertNotNull(challengeRepository.lastSentChallengeId())
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
