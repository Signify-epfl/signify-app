package com.github.se.signify.ui.screens.home

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class HomeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.HOME)
  }

  @Test
  fun allElementsAreDisplayed() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    // Assert that all elements are displayed in ChallengeScreen
    composeTestRule.onNodeWithTag("QuestsButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("CameraFeedbackButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionary").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionaryForward").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExerciseList").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("StreakCounter").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpButton").assertIsDisplayed()
  }

  @Test
  fun allClickableElementsHaveClickActions() {

    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("HelpButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("QuestsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("CameraFeedbackButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("LetterDictionaryBack").assertHasClickAction()
    composeTestRule.onNodeWithTag("LetterDictionaryForward").assertHasClickAction()
  }

  @Test
  fun questsButtonNavigatesToQuestScreen() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("QuestsButton").performClick()

    verify(navigationActions).navigateTo("Quest")
  }

  @Test
  fun cameraFeedbackButtonNavigatesToPracticeScreen() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("CameraFeedbackButton").performClick()

    verify(navigationActions).navigateTo(Screen.PRACTICE)
  }

  // The following 2 tests should be moved to their own file.
  @Test
  fun exerciseListDisplaysExerciseButtons() {
    val exercises = listOf(Exercise("Easy"), Exercise("Medium"))

    composeTestRule.setContent { ExerciseList(exercises) {} }

    exercises.forEach { exercise ->
      composeTestRule
          .onNodeWithTag("${exercise.name}ExerciseButton")
          .performScrollTo()
          .assertIsDisplayed()
    }
  }

  @Test
  fun clickingExerciseButtonsCallsOnClick() {
    val exercises = listOf(Exercise("Easy"), Exercise("Medium"))

    val onClick: (Exercise) -> Unit = mock() // Mock the lambda

    composeTestRule.setContent { ExerciseList(exercises, onClick) }

    exercises.forEach { exercise ->
      composeTestRule.onNodeWithTag("${exercise.name}ExerciseButton").performClick()

      verify(onClick).invoke(exercise) // Verify onClick was called with the exercise
    }
  }

  @Test
  fun initialLetterIsDisplayedCorrectly() {
    composeTestRule.setContent { LetterDictionary() }

    // Assert that the initial letter 'A' and its corresponding icon are displayed
    composeTestRule.onNodeWithTag("LetterText_A").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterIcon_A").assertIsDisplayed()
  }

  @Test
  fun forwardArrowNavigatesThroughAllLettersCorrectly() {
    composeTestRule.setContent { LetterDictionary() }

    val letters = ('A'..'Z').toList()

    letters.forEach { letter ->
      // Assert that the letter and corresponding icon are displayed
      composeTestRule.onNodeWithTag("LetterText_$letter").assertIsDisplayed()
      composeTestRule.onNodeWithTag("LetterIcon_$letter").assertIsDisplayed()

      // Click on the forward arrow to go to the next letter
      composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick()
    }

    // After looping through all letters, it should go back to 'A'
    composeTestRule.onNodeWithTag("LetterText_A").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterIcon_A").assertIsDisplayed()
  }

  @Test
  fun backArrowNavigatesThroughAllLettersCorrectly() {
    composeTestRule.setContent { LetterDictionary() }

    val letters = ('A'..'Z').reversed().toList()

    // Click the back arrow initially to move to 'Z'
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()

    letters.forEach { letter ->
      // Assert that the letter and corresponding icon are displayed
      composeTestRule.onNodeWithTag("LetterText_$letter").assertIsDisplayed()
      composeTestRule.onNodeWithTag("LetterIcon_$letter").assertIsDisplayed()

      // Click on the back arrow to go to the previous letter
      composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()
    }

    // After looping through all letters, it should go back to 'Z'
    composeTestRule.onNodeWithTag("LetterText_Z").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterIcon_Z").assertIsDisplayed()
  }
}
