package com.github.se.signify.ui.screens.tutorial

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.signify.R
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.theme.SignifyTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class TutorialScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var welcomeText: String
  private lateinit var nextButtonText: String
  private lateinit var completionText: String

  private var nextButton = "Next"

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    val context = InstrumentationRegistry.getInstrumentation().targetContext

    welcomeText = context.getString(R.string.tutorial_screen1)
    nextButtonText = context.getString(R.string.tutorial_screen2)
    completionText = context.getString(R.string.tutorial_last_screen)

    composeTestRule.setContent {
      SignifyTheme { TutorialScreen(navigationActions = navigationActions, onFinish = {}) }
    }
  }

  @Test
  fun tutorialScreen_displaysWelcomeTextOnFirstStep() {
    // Assert that the welcome text is displayed
    composeTestRule.onNodeWithText(welcomeText).assertIsDisplayed()

    // Assert that the "Next" button is displayed
    composeTestRule.onNodeWithText(nextButton).assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_highlightsExerciseAreaOnSecondStep() {
    // Click "Next" to go to the second step
    composeTestRule.onNodeWithText(nextButton).performClick()

    // Assert that the exercise area highlight is displayed
    composeTestRule
        .onNodeWithTag("HighlightArea") // Verify using the test tag
        .assertExists()

    // Assert that the second text is displayed
    composeTestRule.onNodeWithText(nextButtonText).assertIsDisplayed()
  }

  @Test
  fun tutorialScreen_displaysCompletionTextOnLastStep() {
    // Navigate to the last step
    repeat(2) { composeTestRule.onNodeWithText(nextButton).performClick() }

    // Assert that the completion text is displayed
    composeTestRule.onNodeWithText(completionText).assertIsDisplayed()
  }
}
