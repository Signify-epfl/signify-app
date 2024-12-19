package com.github.se.signify.end2end

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendsListScreenFeaturesEnd2endTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun friendsListScreenEnd2endTest() {

    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)

    // Wait for navigation to HomeScreen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    // Navigate to Profile Screen
    composeTestRule.onNodeWithTag("Tab_Profile").performClick()
    composeTestRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()

    // Navigate to Friends List Screen
    composeTestRule.onNodeWithTag("MyFriendsButton").performClick()
    composeTestRule.onNodeWithTag("FriendsListScreen").assertIsDisplayed()

    // Verify friends list is displayed
    composeTestRule.onNodeWithText("user3").assertIsDisplayed()

    // Verify friend requests list is displayed
    composeTestRule.onNodeWithTag("RequestsButton").performClick()
    composeTestRule.onNodeWithText("user4").assertIsDisplayed()
    composeTestRule.onNodeWithText("user5").assertIsDisplayed()

    // Accept the request of user 4
    composeTestRule.onAllNodesWithContentDescription("Accept")[0].performClick()
    composeTestRule.onNodeWithText("user4").assertDoesNotExist()

    // Verify user 4 is now a friend
    composeTestRule.onNodeWithTag("FriendsButton").performClick()
    composeTestRule.onNodeWithText("user4").assertIsDisplayed()

    // Decline the request of friend 5
    composeTestRule.onNodeWithTag("RequestsButton").performClick()
    composeTestRule.onAllNodesWithContentDescription("Decline")[0].performClick()
    composeTestRule.onNodeWithText("user5").assertDoesNotExist()

    // Verify user 5 is not a friend
    composeTestRule.onNodeWithTag("FriendsButton").performClick()
    composeTestRule.onNodeWithText("user5").assertDoesNotExist()

    // Remove user 3 from the list of friends
    composeTestRule.onAllNodesWithContentDescription("Remove")[0].performClick()
  }
}
