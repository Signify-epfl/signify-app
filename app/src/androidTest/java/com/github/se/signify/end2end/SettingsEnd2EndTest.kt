package com.github.se.signify.end2end

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.MainActivity
import org.junit.Rule
import org.junit.Test

class SettingsEnd2EndTest {

  // Rule to initialize the Compose test environment for MainActivity
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  // Grant permissions needed for app functionality
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @get:Rule
  val bluetoothAccess: GrantPermissionRule =
      GrantPermissionRule.grant(Manifest.permission.BLUETOOTH)

  @Test
  fun testUserSettingsInteractionFlow() {
    // Constants for test tags and expected values
    val welcomeMessage = "Welcome to Signify"
    val homeScreenTag = "HomeScreen"
    val bottomNavigationTag = "BottomNavigationMenu"
    val profileTabTag = "Tab_Profile"
    val profileScreenTag = "ProfileScreen"
    val settingsButtonTag = "SettingsButton"
    val settingsScreenTag = "SettingsScreen"
    val usernameTextFieldTag = "usernameTextField"
    val newUsername = "NewUsername123"
    val backButtonTag = "BackButton"
    val homeTabTag = "Tab_Home"

    // Step 1: Verify the welcome screen and transition to home screen
    composeTestRule.onNodeWithText(welcomeMessage).assertIsDisplayed()
    composeTestRule.mainClock.advanceTimeBy(7_000) // Simulate delay for transition
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag(homeScreenTag).assertIsDisplayed()

    // Step 2: Navigate to the Profile screen
    composeTestRule.onNodeWithTag(bottomNavigationTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(profileTabTag).performClick()
    composeTestRule.onNodeWithTag(profileScreenTag).assertIsDisplayed()

    // Step 3: Open the Settings screen
    composeTestRule.onNodeWithTag(settingsButtonTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(settingsButtonTag).performClick()
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag(settingsScreenTag, useUnmergedTree = true).assertIsDisplayed()

    // Step 4: Update the username
    composeTestRule.onNodeWithTag(usernameTextFieldTag, useUnmergedTree = true).assertIsDisplayed()
    composeTestRule
        .onNodeWithTag(usernameTextFieldTag, useUnmergedTree = true)
        .performClick()
        .performTextInput(newUsername)

    // Step 5: Simulate the "Done" action on the keyboard
    composeTestRule.onNodeWithTag(usernameTextFieldTag).performImeAction()

    // Step 6: Verify the username has been updated
    composeTestRule
        .onNodeWithTag(usernameTextFieldTag, useUnmergedTree = true)
        .assertTextContains(newUsername, substring = true)

    // TODO: Implement update and delete profile photo functionality in the test

    // Step 7: Return to the Profile screen
    composeTestRule.onNodeWithTag(backButtonTag, useUnmergedTree = true).performClick()
    composeTestRule.onNodeWithTag(profileScreenTag).assertIsDisplayed()

    // Step 8: Navigate back to the Home screen
    composeTestRule.onNodeWithTag(homeTabTag).performClick()
    composeTestRule.onNodeWithTag(homeScreenTag).assertIsDisplayed()
  }
}
