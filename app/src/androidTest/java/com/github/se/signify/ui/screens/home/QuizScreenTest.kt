package com.github.se.signify.ui.screens.home

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.common.getIconResId
import com.github.se.signify.model.home.quiz.QuizQuestion
import com.github.se.signify.model.home.quiz.QuizRepository
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@Suppress("UNCHECKED_CAST")
class QuizScreenComponentsTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var mockQuizRepository: QuizRepository
  private lateinit var mockNavigationActions: NavigationActions

  private val testQuiz =
      QuizQuestion(
          correctWord = "car",
          confusers = listOf("cat", "cup"),
          signs = listOf(getIconResId('c'), getIconResId('a'), getIconResId('r')))

  @Before
  fun setUp() {
    mockQuizRepository = mock(QuizRepository::class.java)
    mockNavigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun quizHeader_displaysTitleAndBackButton() {
    composeTestRule.setContent { QuizScreen(mockNavigationActions, mockQuizRepository) }

    // Verify header components
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuizTitle").assertIsDisplayed()
  }

  @Test
  fun quizHeader_backButtonTriggersNavigation() {
    composeTestRule.setContent { QuizScreen(mockNavigationActions, mockQuizRepository) }

    // Click back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Verify navigation action
    verify(mockNavigationActions).goBack()
  }

  @Test
  fun quizContent_displaysSignsAndOptions() {
    composeTestRule.setContent {
      QuizContent(
          currentQuiz = testQuiz,
          shuffledOptions = listOf("car", "cat", "cup"),
          selectedOption = null,
          onOptionSelected = {},
          onSubmit = {})
    }

    // Verify signs
    composeTestRule.onNodeWithTag("SignsRow").assertIsDisplayed()
    composeTestRule.onAllNodesWithTag("SignImage").assertCountEquals(3)

    // Verify options
    composeTestRule.onNodeWithTag("OptionsColumn").assertIsDisplayed()
    composeTestRule.onAllNodesWithTag("OptionRow").assertCountEquals(3)
  }

  @Test
  fun quizContent_submitButtonDisabledInitially() {
    composeTestRule.setContent {
      QuizContent(
          currentQuiz = testQuiz,
          shuffledOptions = listOf("car", "cat", "cup"),
          selectedOption = null,
          onOptionSelected = {},
          onSubmit = {})
    }

    // Verify submit button is disabled
    composeTestRule.onNodeWithTag("SubmitButton").assertIsNotEnabled()
  }
}
