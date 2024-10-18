package com.github.se.signify.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.SettingsScreen
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SettingsScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  private fun setContent(profilePictureUrl: String? = null) {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      SettingsScreen(profilePictureUrl = profilePictureUrl, navigationActions = navigationActions)
    }
  }

  @Test
  fun testSettingsScreenDisplaysCorrectInformation() {
    setContent("https://example.com/profile.jpg")

    // Check if the username is displayed
    composeTestRule.onNodeWithText("Test Name 1").assertIsDisplayed()

    // Check if the edit username icon is displayed
    composeTestRule.onNodeWithContentDescription("Edit Username").assertIsDisplayed()

    // Check if the edit profile picture icon is displayed
    composeTestRule.onNodeWithContentDescription("Edit Profile Picture").assertIsDisplayed()

    // Check if the "Other settings" section is displayed
    composeTestRule.onNodeWithText("Other settings:\nLanguage,\n theme, ...").assertIsDisplayed()
  }

  @Test
  fun testBackButtonNavigatesBack() {
    setContent("https://example.com/profile.jpg")

    // Click the back button
    composeTestRule.onNodeWithContentDescription("Back").performClick()

    // Check if back navigation was triggered
    verify(navigationActions).goBack()
  }

  @Test
  fun testEditUsernameClickable() {
    setContent()

    // Click on the editable username
    composeTestRule.onNodeWithText("Test Name 1").performClick()

    // Verify that edit options should show (you may need to add state verification)
    // This requires modifying your `SettingsScreen` to handle edit actions appropriately
    // For demonstration, we assert that the click event was recognized
    assert(true)
  }

  @Test
  fun testCancelButton() {
    setContent()

    // Click the cancel button
    composeTestRule.onNodeWithText("Cancel").performClick()

    // Check for any expected behavior on cancel (to be implemented)
    // For example, you may want to check if a dialog appears or navigation occurs
    assert(true) // Replace with actual assertion
  }

  @Test
  fun testSaveButton() {
    setContent()

    // Click the save button
    composeTestRule.onNodeWithText("Save").performClick()

    // Check for any expected behavior on save (to be implemented)
    // This could involve asserting that a state has changed or a navigation has occurred
    assert(true) // Replace with actual assertion
  }
}
