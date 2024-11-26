package com.github.se.signify.ui.screens.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.user.User
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq

class FriendsListScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private val currentFriends = mutableStateListOf("Alice", "Bob", "Charlie", "Sam")
  private val friendRequests = mutableStateListOf("Dave", "Eve", "Daniel", "Leo")
  private val testUser =
      User(
          uid = "testUserId",
          name = "Test User",
          currentStreak = 0L,
          highestStreak = 0L) // Test user data
  private val searchBar = "SearchBar"
  private val friendsList = "My friends list"
  private val friendsDemands = "New friends demands"
  private val search = "Search"

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)

    // Mock getFriendsList method to return currentFriends
    doAnswer { invocation ->
          // This callback is assumed to be the second argument
          val onSuccess = invocation.arguments[1] as (List<String>) -> Unit
          onSuccess(currentFriends)
        }
        .`when`(userRepository)
        .getFriendsList(Mockito.anyString(), anyOrNull(), anyOrNull())

    // Mock getRequestsFriendsList method to return friendRequests
    doAnswer { invocation ->
          // Ensure the correct argument index for the onSuccess callback
          val onSuccess = invocation.arguments[1] as (List<String>) -> Unit
          onSuccess(friendRequests)
        }
        .`when`(userRepository)
        .getRequestsFriendsList(Mockito.anyString(), anyOrNull(), anyOrNull())

    userViewModel = UserViewModel(userSession, userRepository)

    composeTestRule.setContent {
      FriendsListScreen(navigationActions, userSession, userRepository, userViewModel)
    }
  }

  @Test
  fun testFriendsListScreenDisplaysCorrectInformation() {
    // Verify top blue bar is displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()

    // Verify top information are displayed
    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DefaultProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertIsDisplayed()

    // Check if the Friends List title is displayed
    composeTestRule.onNodeWithText(friendsList).assertIsDisplayed()

    // Check if all friends are displayed
    currentFriends.forEachIndexed { index, friend ->
      composeTestRule.onNodeWithTag(friendsList).performScrollToIndex(index)
      composeTestRule.waitForIdle()
      composeTestRule.onNodeWithText(friend).assertIsDisplayed()
    }

    // Check if the Friend Requests title is displayed
    composeTestRule.onNodeWithText(friendsDemands).assertIsDisplayed()

    // Check if all friend requests are displayed
    friendRequests.forEachIndexed { index, request ->
      composeTestRule.onNodeWithTag(friendsDemands).performScrollToIndex(index)
      composeTestRule.waitForIdle()
      composeTestRule.onNodeWithText(request).assertIsDisplayed()
    }
  }

  @Test
  fun pressingBackArrowNavigatesToProfileScreen() {

    // Click the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Verify the back button performed well
    verify(navigationActions).goBack()
  }

  @Test
  fun displayEmptyFriendsList() {
    // Clear friends list to simulate an empty state
    currentFriends.clear()
    composeTestRule.waitForIdle()

    // Verify empty message is displayed
    composeTestRule.onNodeWithText("You have no friends").assertIsDisplayed()
  }

  @Test
  fun displayEmptyFriendRequests() {
    // Use an empty list for friend requests
    friendRequests.clear()
    composeTestRule.waitForIdle()

    // Verify empty message is displayed for friend requests
    composeTestRule.onNodeWithText("No new friend requests").assertIsDisplayed()
  }

  @Test
  fun testRemoveFriend() {
    val friendName = "Alice"
    // Scroll to the specific request
    composeTestRule
        .onNodeWithTag(friendsList)
        .performScrollToIndex(currentFriends.indexOf(friendName))
    composeTestRule.waitForIdle()

    // Target the accept button based on index
    composeTestRule.onAllNodesWithText("Remove")[currentFriends.indexOf(friendName)].performClick()
    verify(userRepository)
        .removeFriend(Mockito.anyString(), eq(friendName), anyOrNull(), anyOrNull())
  }

  @Test
  fun testAcceptFriendRequest() {
    val requestName = "Dave"
    composeTestRule
        .onNodeWithTag(friendsDemands)
        .performScrollToIndex(friendRequests.indexOf(requestName))
    composeTestRule.waitForIdle()

    composeTestRule
        .onAllNodesWithContentDescription("Accept")[friendRequests.indexOf(requestName)]
        .performClick()
    verify(userRepository)
        .acceptFriendRequest(Mockito.anyString(), eq(requestName), anyOrNull(), anyOrNull())
  }

  @Test
  fun testDeclineFriendRequest() {
    val requestName = "Dave"
    composeTestRule
        .onNodeWithTag(friendsDemands)
        .performScrollToIndex(friendRequests.indexOf(requestName))
    composeTestRule.waitForIdle()

    composeTestRule
        .onAllNodesWithContentDescription("Decline")[friendRequests.indexOf(requestName)]
        .performClick()
    verify(userRepository)
        .declineFriendRequest(Mockito.anyString(), eq(requestName), anyOrNull(), anyOrNull())
  }

  @Test
  fun searchAndDisplayUser() {
    userViewModel.setSearchResult(testUser) // Simulate a successful search result

    // Act
    composeTestRule.onNodeWithTag(searchBar).performTextInput(testUser.uid)
    composeTestRule.onNodeWithContentDescription(search).performClick()

    // Assert
    composeTestRule
        .onNodeWithText("Test User")
        .assertIsDisplayed() // Check if dialog displays user name
  }

  @Test
  fun addFriendAfterSearch() {

    userViewModel.setSearchResult(testUser) // Simulate a successful search result

    // Act
    composeTestRule.onNodeWithTag(searchBar).performTextInput(testUser.uid)
    composeTestRule.onNodeWithContentDescription(search).performClick()

    // Assert
    // Click "Add Friend" button
    composeTestRule.onNodeWithText("Add Friend").performClick()
    // Verify sendFriendRequest was called with the correct parameters
    verify(userRepository)
        .sendFriendRequest(Mockito.anyString(), eq(testUser.uid), anyOrNull(), anyOrNull())
    // Verify that searchResult was reset to null
    assertNull(userViewModel.searchResult.value)
  }

  @Test
  fun removeFriendAfterSearch() {
    // Arrange
    val friendUser =
        User(
            uid = "Alice",
            name = "Friend User",
            currentStreak = 0L,
            highestStreak = 0L) // Simulated friend user data
    userViewModel.setSearchResult(friendUser) // Simulate search result with existing friend

    // Act
    composeTestRule.onNodeWithTag(searchBar).performTextInput(friendUser.uid)
    composeTestRule.onNodeWithContentDescription(search).performClick()

    // Assert dialog shows "Remove Friend" button as they are friends
    composeTestRule.onNodeWithText("Remove Friend").assertIsDisplayed()

    // Perform remove action and check if friend is removed
    composeTestRule.onNodeWithText("Remove Friend").performClick()
    verify(userRepository)
        .removeFriend(Mockito.anyString(), eq(friendUser.uid), anyOrNull(), anyOrNull())
    // Verify that searchResult was reset to null
    assertNull(userViewModel.searchResult.value)
  }

  @Test
  fun testMyProfileButtonForAuthenticatedUser() {
    // Arrange
    // Create a User instance matching the authenticated user's ID
    val authenticatedUser =
        User(
            uid = userSession.getUserId()!!,
            name = "Authenticated User",
            currentStreak = 10L,
            highestStreak = 20L)

    // Set the search result to simulate the authenticated user
    userViewModel.setSearchResult(authenticatedUser)

    // Act
    // Click the "My Profile" button (it should now be visible)
    composeTestRule.onNodeWithText("My Profile").performClick()
    composeTestRule.onNodeWithText(authenticatedUser.name!!).isDisplayed()

    // Assert
    // Verify navigation to the PROFILE screen was triggered
    verify(navigationActions).navigateTo(Screen.PROFILE)

    // Verify that searchResult was reset to null
    assertNull(userViewModel.searchResult.value)
  }

  @Test
  fun closeDialogAfterSearch() {

    userViewModel.setSearchResult(testUser) // Simulate a successful search result

    // Act
    composeTestRule.onNodeWithTag(searchBar).performTextInput(testUser.uid)
    composeTestRule.onNodeWithContentDescription(search).performClick()

    // Assert
    // Click "Close" button
    composeTestRule.onNodeWithText("Close").performClick()
    // Verify dialog is dismissed
    composeTestRule.onNodeWithText("Close").assertDoesNotExist()
  }

  @Test
  fun searchInvalidUser() {
    // Arrange
    val invalidUserId = "nonexistentUser" // // Simulate a non-existing user ID

    // Simulate a non successful search
    userViewModel.setSearchResult(null) // Initial state
    userViewModel.setErrorState("User not found")
    composeTestRule.waitForIdle()

    // Act

    composeTestRule.onNodeWithTag(searchBar).performTextInput(invalidUserId)
    composeTestRule.onNodeWithContentDescription(search).performClick()
    composeTestRule.waitForIdle()

    // Assert
    assertNull(userViewModel.searchResult.value) // Verify that searchResult is set to null
  }
}
