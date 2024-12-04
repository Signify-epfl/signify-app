package end2end

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExerciseEnd2endTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setup() {
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()
  }

  @Test
  fun exerciseFeatureTest() {
    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("skipLoginButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    // Click on the EasyExercise
    composeTestRule.onNodeWithTag("EasyExerciseButtonText", useUnmergedTree = true).performClick()
    composeTestRule.onNodeWithTag("ExerciseScreenEasy", useUnmergedTree = true).assertIsDisplayed()
    // Advance by 7 seconds, at this point the user finished the easy exercise
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("BackButton").performClick()
    composeTestRule
        .onNodeWithTag("MediumExerciseButtonText", useUnmergedTree = true)
        .performScrollTo()
        .performClick()
    composeTestRule
        .onNodeWithTag("ExerciseScreenMedium", useUnmergedTree = true)
        .assertIsDisplayed()
    composeTestRule.mainClock.advanceTimeBy(7_000)
    // Advance by 7 seconds, at this point the user finished the medium exercise
    composeTestRule.onNodeWithTag("BackButton").performClick()

    composeTestRule
        .onNodeWithTag("HardExerciseButtonText", useUnmergedTree = true)
        .performScrollTo()
        .performClick()
    composeTestRule.onNodeWithTag("ExerciseScreenHard", useUnmergedTree = true).assertIsDisplayed()
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.waitForIdle()
    // Advance by 7 seconds, at this point the user finished the Hard exercise
    composeTestRule.onNodeWithTag("BackButton").performClick()

    composeTestRule.waitForIdle()
    // The user finished his exercises
  }

  @Test
  fun questFeatureTest() {

    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("skipLoginButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsButton").performClick()
    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()

    composeTestRule.onNodeWithTag("BackButton").performClick()

    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuizButton").performClick()
    composeTestRule.onNodeWithTag("QuizTitle").assertIsDisplayed()

    composeTestRule.onNodeWithTag("BackButton").performClick()
    // The user want to give feedback
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FeedbackButton").performClick()
    composeTestRule.onNodeWithTag("FeedbackScreenContent").assertIsDisplayed()
  }
}
