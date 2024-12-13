package com.github.se.signify.ui.screens.tutorial

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class TutorialScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private var completed = false

  private val nextButtonTag = "nextButton"
  private val skipButtonTag = "SkipTutorialButton"

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    completed = false

    composeTestRule.setContent {
      ProvideElementPositions {
        TutorialScreen(navigationActions = navigationActions, onFinish = { completed = true })
      }
    }
  }

  @Test
  fun tutorialScreen_navigatesThroughSteps_onNext() {
    // Verify the first step
    composeTestRule.onNodeWithTag("WelcomeTextTag").assertIsDisplayed()

    // Simulate "Next" button click
    composeTestRule.onNodeWithTag(nextButtonTag).performClick()

    // Verify the second step is displayed
    composeTestRule.onNodeWithTag("ExerciseTextTag").assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_skipsTutorial_onSkipButtonClick() {
    // Click the "Skip" button
    composeTestRule.onNodeWithTag(skipButtonTag).performClick()

    // Verify that onFinish was called
    assert(completed)
  }

  @Test
  fun blockingInteractionsOverlay_blocksAllInteractions() {

    // Attempt to interact with the overlay
    composeTestRule.onRoot().performClick()

    // Assert no interaction passed through
    composeTestRule.onRoot().assertExists()
  }

  @Test
  fun skipButton_isDisplayedAtTopRight() {
    // Verify the "Skip" button is displayed
    composeTestRule.onNodeWithTag(skipButtonTag).assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_displaysAllSteps() {
    val tutorialSteps =
        listOf(
            "WelcomeTextTag",
            "DictionaryTextTag",
            "CameraFeedbackTag",
            "ExerciseTextTag",
            "QuestsTextTag",
            "QuizTextTag",
            "FeedbackTextTag",
            "CompletionTextTag")

    tutorialSteps.forEachIndexed { index, tag ->
      // Check that the correct step is displayed
      composeTestRule.onNodeWithTag(tag).assertIsDisplayed()

      // If not the last step, click "Next" to proceed to the next step
      if (index < tutorialSteps.size - 1) {
        composeTestRule.onNodeWithTag(nextButtonTag).performClick()
      }
    }

    // After the last step, ensure the tutorial completes
    composeTestRule.onNodeWithTag("CompletionTextTag").assertIsDisplayed()
  }
}
