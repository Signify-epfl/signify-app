package com.github.se.signify.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.FriendsListScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class FriendsListScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private val currentFriends = mutableStateListOf("Alice", "Bob", "Charlie")
  private val friendRequests = mutableStateListOf("Dave", "Eve")
  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      FriendsListScreen(
          currentFriends = currentFriends,
          friendRequests = friendRequests,
          onRemoveFriend = { friend -> currentFriends.remove(friend) },
          onAcceptFriendRequest = { request -> friendRequests.remove(request) },
          onRejectFriendRequest = { request -> friendRequests.remove(request) },
          onSearchUser = {},
          navigationActions = navigationActions)
    }
  }

  @Test
  fun testFriendsListScreenDisplaysCorrectInformation() {

    // Check if the Friends List title is displayed
    composeTestRule.onNodeWithText("My friends list").assertIsDisplayed()

    // Check if all friends are displayed
    currentFriends.forEach { friend -> composeTestRule.onNodeWithText(friend).assertIsDisplayed() }

    // Check if the Friend Requests title is displayed
    composeTestRule.onNodeWithText("New friends demand").assertIsDisplayed()

    // Check if all friend requests are displayed
    friendRequests.forEach { request ->
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
}
