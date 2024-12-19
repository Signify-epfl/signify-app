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
class FriendsListAndChallengeFeaturesEnd2endTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun friendsList_Challenge_ScreenEnd2endTest() {

    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)

    // Wait for navigation to HomeScreen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    // Navigate to Challenge Screen
    composeTestRule.onNodeWithTag("Tab_Challenge").performClick()
    composeTestRule.onNodeWithTag("ChallengeScreen").assertIsDisplayed()

    // Navigate to New Challenge Screen
    composeTestRule.onNodeWithTag("ChallengeButton").performClick()
    composeTestRule.onNodeWithTag("NewChallengeScreen").assertIsDisplayed()

    // Verify that we have a friend
    composeTestRule.onNodeWithTag("MyFriendsButton").performClick()
    composeTestRule.onNodeWithTag("FriendsListScreen").assertIsDisplayed()
    composeTestRule.onNodeWithText("user3").assertIsDisplayed()

    // Navigate to Create a Challenge Screen
    composeTestRule.onNodeWithTag("BackButton").performClick()
    composeTestRule.onNodeWithTag("NewChallengeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CreateChallengeButton").performClick()
    composeTestRule.onNodeWithTag("CreateAChallengeContent").assertIsDisplayed()

    // Create a Sprint challenge with user3
    composeTestRule.onNodeWithTag("ChallengeButton_user3").performClick()
    composeTestRule.onNodeWithTag("SprintModeButton").performClick()
    composeTestRule.onNodeWithTag("SendChallengeButton").performClick()

    // Navigate back to New Challenge Screen
    composeTestRule.onNodeWithTag("BackButton").performClick()
    composeTestRule.onNodeWithTag("NewChallengeScreen").assertIsDisplayed()

    // Verify the created challenge is displayed
    // composeTestRule.onNodeWithText("Opponent").assertIsDisplayed()
    // composeTestRule.onNodeWithText("user3").assertIsDisplayed()
    // composeTestRule.onNodeWithText("Mode : SPRINT").assertIsDisplayed()

    // Navigate back to Challenge Screen
    composeTestRule.onNodeWithTag("BackButton").performClick()
    composeTestRule.onNodeWithTag("ChallengeScreen").assertIsDisplayed()

    // Navigate to History Challenge Screen
    composeTestRule.onNodeWithTag("HistoryButton").performClick()
    composeTestRule.onNodeWithTag("ChallengeHistoryScreen").assertIsDisplayed()

    // Verify the number of created challenges is 1
    // TODO : check the number of created

    // Navigate back to Challenge Screen
    composeTestRule.onNodeWithTag("BackButton").performClick()
    composeTestRule.onNodeWithTag("ChallengeScreen").assertIsDisplayed()

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
