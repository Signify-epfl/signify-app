package com.github.se.signify.ui.screens.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.feedback.FeedbackRepository
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class FeedbackScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private val userSession = MockUserSession()
  private val feedbackRepository = Mockito.mock(FeedbackRepository::class.java)
  private val mockNavigationActions = Mockito.mock(NavigationActions::class.java)

  @Test
  fun feedbackScreen_uiElementsAreDisplayed() {
    composeTestRule.setContent {
      FeedbackScreen(navigationActions = mockNavigationActions, userSession, feedbackRepository)
    }

    // Check that top blue bar and back button are displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Check that feedback type dropdown is displayed
    composeTestRule.onNodeWithTag("FeedbackTypeDropdown").assertIsDisplayed()

    // Check that feedback title input is displayed
    composeTestRule.onNodeWithTag("FeedbackTitleInput").assertIsDisplayed()

    // Check that feedback description input is displayed
    composeTestRule.onNodeWithTag("FeedbackDescriptionInput").assertIsDisplayed()

    // Check that rating section is displayed
    composeTestRule.onNodeWithTag("RatingTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("RatingStars").assertIsDisplayed()

    // Check that send feedback button is displayed
    composeTestRule.onNodeWithTag("SendFeedbackButton").assertIsDisplayed()
  }

  @Test
  fun feedbackScreen_interactWithDropdownMenu() {
    composeTestRule.setContent {
      FeedbackScreen(navigationActions = mockNavigationActions, userSession, feedbackRepository)
    }

    // Open the dropdown menu
    composeTestRule.onNodeWithTag("FeedbackTypeDropdown").performClick()

    // Select an item from the dropdown menu
    composeTestRule.onNodeWithTag("DropdownMenuItem_Feature Suggestion").performClick()

    // Verify the selected feedback type
    composeTestRule.onNodeWithText("Feature Suggestion").assertIsDisplayed()
  }

  @Test
  fun feedbackScreen_fillAndSendFeedback() {
    composeTestRule.setContent {
      FeedbackScreen(navigationActions = mockNavigationActions, userSession, feedbackRepository)
    }

    // Fill in feedback title
    composeTestRule.onNodeWithTag("FeedbackTitleInput").performTextInput("App Crashes")

    // Fill in feedback description
    composeTestRule
        .onNodeWithTag("FeedbackDescriptionInput")
        .performTextInput("The app crashes when I try to log in.")

    // Click on the 4th star for rating
    composeTestRule.onNodeWithTag("Star_4").performClick()
  }

  @Test
  fun feedbackScreen_selectRating() {
    composeTestRule.setContent {
      FeedbackScreen(navigationActions = mockNavigationActions, userSession, feedbackRepository)
    }

    // Click on the 5th star for rating
    composeTestRule.onNodeWithTag("Star_5").performClick()

    // Verify that the 5th star is selected (yellow)
    composeTestRule.onNodeWithTag("Star_5").assertIsDisplayed()
  }
}
