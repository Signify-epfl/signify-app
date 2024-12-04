package end2end

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PracticeEnd2endTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setup() {
    // Assert Welcome Screen is displayed
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()

    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()
  }

  @Test
  fun navigateToASLRecognitionScreen() {

    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("skipLoginButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    // The "user" searches for the letters d - o - g so that he can navigate to the ASL Recognition
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()
    var index = 4 // letter d
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_D").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_D").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    index = 11 // letter o
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_O").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_O").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    index = 8 // letter g
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_G").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_G").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    // After searching, the user will go to the try out button to exercise him self
    composeTestRule.onNodeWithTag("CameraFeedbackButton").assertIsDisplayed().performClick()

    // Go to ASLRecognition screen
    composeTestRule.onNodeWithTag("cameraPreview").assertIsDisplayed()
    composeTestRule.onNodeWithTag("gestureOverlayView").assertIsDisplayed()
    composeTestRule.waitForIdle()
  }
}
