import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.quiz.QuizQuestion
import com.github.se.signify.model.quiz.QuizRepository
import com.github.se.signify.ui.getIconResId
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.home.NoQuizAvailable
import com.github.se.signify.ui.screens.home.QuizContent
import com.github.se.signify.ui.screens.home.QuizHeader
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class QuizScreenComponentsTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var mockQuizRepository: QuizRepository
  private lateinit var mockNavigationActions: NavigationActions

  private val testQuiz =
      QuizQuestion(
          correctWord = "car",
          confusers = listOf("cat", "cup"),
          signs =
              listOf(
                  getIconResId('c'),
                  getIconResId('a'),
                  getIconResId('r')) // Mocked image resource IDs
          )

  @Before
  fun setUp() {
    mockQuizRepository = mock(QuizRepository::class.java)
    mockNavigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun quizHeader_displaysTitleAndBackButton() {
    composeTestRule.setContent { QuizHeader(navigationActions = mockNavigationActions) }

    // Verify header components
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuizTitle").assertIsDisplayed()
  }

  @Test
  fun quizHeader_backButtonTriggersNavigation() {
    composeTestRule.setContent { QuizHeader(navigationActions = mockNavigationActions) }

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

  @Test
  fun noQuizAvailable_displaysMessage() {
    composeTestRule.setContent { NoQuizAvailable() }

    // Verify "No quizzes available" message
    composeTestRule.onNodeWithTag("NoQuizContainer").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NoQuizzesText").assertTextEquals("No quizzes available.")
  }
}
