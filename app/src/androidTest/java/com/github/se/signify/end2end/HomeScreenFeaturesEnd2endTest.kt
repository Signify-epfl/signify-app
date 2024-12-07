package com.github.se.signify.end2end

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeScreenFeaturesEnd2endTest {
  @get:Rule
  val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)
  @get:Rule
  val bluetoothAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.BLUETOOTH)

  @Before
  fun setup() {
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
  }
  @After
  fun tearDown() {
    composeTestRule.activity.resources.assets.close()
    composeTestRule.activity.cacheDir.delete()
    composeTestRule.activityRule.scenario.close()
  }

}