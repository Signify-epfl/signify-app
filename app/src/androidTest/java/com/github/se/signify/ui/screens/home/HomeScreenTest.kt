package com.github.se.signify.ui.screens.home

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.se.signify.model.home.exercise.ExerciseLevel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
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

    `when`(navigationActions.currentRoute()).thenReturn(Screen.HOME.route)
  }

  @Test
  fun allElementsAreDisplayed() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    // Assert that all elements are displayed in ChallengeScreen
    composeTestRule.onNodeWithTag("QuestsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FeedbackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuizButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CameraFeedbackButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionary").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("LetterDictionaryForward").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExerciseListPager").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun allClickableElementsHaveClickActions() {

    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("QuestsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("CameraFeedbackButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("LetterDictionaryBack").assertHasClickAction()
    composeTestRule.onNodeWithTag("LetterDictionaryForward").assertHasClickAction()
  }

  @Test
  fun allClickableElementsPerformClick() {

    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("QuestsButton").performClick()
    composeTestRule.onNodeWithTag("CameraFeedbackButton").performClick()
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()
    composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick()
  }

  @Test
  fun questsButtonNavigatesToQuestScreen() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("QuestsButton").performClick()

    verify(navigationActions).navigateTo(Screen.QUEST)
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
    val exercises = ExerciseLevel.entries

    composeTestRule.setContent { ExerciseList(exercises, navigationActions) }

    exercises.forEach { exercise ->
      composeTestRule
          .onNodeWithTag("${exercise.id}ExerciseButton")
          .performScrollTo()
          .assertIsDisplayed()
    }
  }

  @Test
  fun exerciseButtonTextDisplaysCorrectly() {
    val exercises = ExerciseLevel.entries

    composeTestRule.setContent { ExerciseList(exercises, navigationActions) }

    exercises.forEach { exercise ->
      composeTestRule
          .onNodeWithTag("${exercise.id}ExerciseButtonText", useUnmergedTree = true)
          .performScrollTo()
          .assertIsDisplayed()
          .assertTextEquals(exercise.id)
    }
  }

  @Test
  fun clickingExerciseButtonsCallsOnClick() {
    val exercises = ExerciseLevel.entries

    composeTestRule.setContent { ExerciseList(exercises, navigationActions) }

    exercises.forEach { exercise ->
      composeTestRule.onNodeWithTag("${exercise.id}ExerciseButton").performScrollTo().performClick()

      verify(navigationActions)
          .navigateTo(exercise.screen) // Verify onClick was called with the exercise
    }
  }

  @Test
  fun dictionaryIsDisplayedUsingButtons() {
    composeTestRule.setContent { HomeScreen(navigationActions) }

    ('A'..'Z').forEachIndexed { index, letter ->
      // Click on "LetterDictionaryForward" to navigate to the desired letter
      if (index > 0) {
        composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick()
      }
      // Click on the specific letter box
      composeTestRule.onNodeWithTag("LetterBox_$letter").performClick()

      // Assert that the text and corresponding sign tip are displayed
      composeTestRule.onNodeWithTag("LetterTextDict_$letter").assertIsDisplayed()
      composeTestRule.onNodeWithTag("SignTipBox_$letter").assertIsDisplayed()

      // Scroll to top button is clicked
      composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    }
  }

  @Test
  fun dictionaryIsDisplayedUsingPagersScrolling() {
    composeTestRule.setContent { HomeScreen(navigationActions) }

    ('A'..'Z').forEachIndexed { _, letter ->
      // Click on the specific letter box by scrolling to it
      composeTestRule.onNodeWithTag("LetterBox_$letter").performScrollTo().performClick()

      // Assert that the text and corresponding sign tip are displayed
      composeTestRule.onNodeWithTag("LetterTextDict_$letter").assertIsDisplayed()
      composeTestRule.onNodeWithTag("SignTipBox_$letter").assertIsDisplayed()

      // Scroll to top button is clicked
      composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    }
  }
}
