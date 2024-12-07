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

class HomeScreenFeaturesEnd2endTest {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setup() {
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()
    composeTestRule.waitForIdle()
  }

  @Test
  fun questFeatureTest() {

    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("loginButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsButton").performClick()
    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()
    // After finishing the task go back
    composeTestRule.onNodeWithTag("BackButton").performClick()
    // The user wants to do a quiz
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuizButton").performClick()
    composeTestRule.onNodeWithTag("QuizTitle").assertIsDisplayed()
    // After finishing the task go back
    composeTestRule.onNodeWithTag("BackButton").performClick()

    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsButton").performClick()
    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()
    // After finishing the task go back
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // The user wants to give feedback
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FeedbackButton").performClick()
    composeTestRule.onNodeWithTag("FeedbackScreen").assertIsDisplayed()
    composeTestRule.activity.finish()
  }
}