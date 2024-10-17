package com.github.se.signify.ui.screens

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.Home.Exercise
import com.github.se.signify.ui.screens.Home.ExerciseList
import com.github.se.signify.ui.screens.Home.HomeScreen
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
    composeTestRule.onNodeWithTag("CameraFeedbackToggle").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("CameraFeedback").performScrollTo().assertIsDisplayed()
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
    composeTestRule.onNodeWithTag("CameraFeedbackToggle").assertHasClickAction()
    composeTestRule.onNodeWithTag("CameraFeedback").assertHasClickAction()
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
  fun cameraFeedbackWindowNavigatesToMainAimScreen() {
    composeTestRule.setContent { HomeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("CameraFeedback").performClick()

    verify(navigationActions).navigateTo("MainAim")
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
}
