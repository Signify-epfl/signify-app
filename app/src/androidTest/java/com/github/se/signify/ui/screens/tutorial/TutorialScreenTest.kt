package com.github.se.signify.ui.screens.tutorial

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.theme.SignifyTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class TutorialScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  private val welcomeTextTag = "WelcomeTextTag"
  private val exerciseTextTag = "ExerciseTextTag"
  private val completionTextTag = "CompletionTextTag"

  private val nextButtonTag = "nextButton"

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    composeTestRule.setContent {
      SignifyTheme { TutorialScreen(navigationActions = navigationActions, onFinish = {}) }
    }
  }

  @Test
  fun tutorialScreen_displaysWelcomeTextOnFirstStep() {
    // Assert that the welcome text is displayed
    composeTestRule.onNodeWithTag(welcomeTextTag).assertIsDisplayed()

    // Assert that the "Next" button is displayed
    composeTestRule.onNodeWithTag(nextButtonTag).assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_highlightsExerciseAreaOnSecondStep() {
    // Click "Next" to go to the second step
    composeTestRule.onNodeWithTag(nextButtonTag).performClick()

    // Assert that the exercise area highlight is displayed
    composeTestRule
        .onNodeWithTag("HighlightArea") // Verify using the test tag
        .assertExists()

    // Assert that the second text is displayed
    composeTestRule.onNodeWithTag(exerciseTextTag).assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_displaysCompletionTextOnLastStep() {
    // Navigate to the last step
    repeat(2) { composeTestRule.onNodeWithTag(nextButtonTag).performClick() }

    // Assert that the completion text is displayed
    composeTestRule.onNodeWithTag(completionTextTag).assertIsDisplayed()
  }
}
