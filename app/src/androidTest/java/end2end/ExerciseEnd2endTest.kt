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
import org.junit.Rule
import org.junit.Test

class ExerciseEnd2endTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Test
  fun exerciseFeatureTest() {
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()

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
    composeTestRule.onNodeWithTag("BackButton").performClick()
  }

  @Test
  fun questFeatureTest() {
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()

    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("skipLoginButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    // Go to the profile
    composeTestRule.onNodeWithTag("TabIcon_Profile", useUnmergedTree = true).performClick()
    composeTestRule.onNodeWithTag("UnauthenticatedScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("logInButton").performClick()
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("loginButton").performClick()
  }
}
