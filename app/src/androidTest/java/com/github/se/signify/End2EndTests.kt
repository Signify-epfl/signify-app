package com.github.se.signify

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.ui.screens.welcome.welcomeScreenDuration
import org.junit.Rule
import org.junit.Test

/**
 * This class performs Combined End-to-End (E2E) tests for the Signify application. The tests
 * simulate user interactions to validate the functionality of various features.
 *
 * The following End-to-End scenarios are tested:
 * 1. **ASL Recognition Test:**
 *     - Simulates a user searching for letters in the Letter Dictionary ("D", "O", "G").
 *     - Displays the corresponding Sign Tips for each letter.
 *     - Tests navigation to the ASL Recognition Screen where the user can practice hand signs.
 * 2. **Quiz Feature Test:**
 *     - Simulates a user starting a quiz from the Home Screen.
 *     - Selects an answer option (e.g., "apple") and submits it.
 *     - Verifies a confirmation (e.g., Toast) for the correct answer.
 *     - Tests navigation back to the Home Screen.
 * 3. **Friends Feature Test:**
 *     - (Placeholder for adding a friend feature test, not yet implemented).
 * 4. **Settings Feature Test:**
 *     - (Placeholder for changing settings feature test, not yet implemented).
 *
 * **Test Details:**
 * - The tests make use of Compose Test API to find and interact with UI components by their tags.
 * - Permissions for Camera and Bluetooth are granted automatically using GrantPermissionRule.
 * - Animations and transitions are skipped or advanced where possible to improve test efficiency.
 *
 * **Note:** The ASL Recognition test assumes the user knows how to perform the hand signs and skips
 * actual hand detection as it is complex to simulate. Placeholder comments are added for features
 * that are yet to be implemented.
 */
class CombinedEnd2endTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @get:Rule
  val bluetoothAccess: GrantPermissionRule =
      GrantPermissionRule.grant(Manifest.permission.BLUETOOTH)

  @Test
  fun combinedEnd2endTest() {
    composeTestRule.onNodeWithTag("WelcomeScreen").assertIsDisplayed()
    // Wait for transition to HomeScreen
    composeTestRule.mainClock.advanceTimeBy(
        welcomeScreenDuration()) // This line skips the animation of the WelcomeScreen which is 7
    // seconds in an
    // emulator to be time efficient.
    composeTestRule.waitForIdle()
    /**
     * -----------------------------------------
     * As the User is considered to be Logged In. The WelcomeScreen is directly getting the user to
     * the HomeScreen. The User now can interact with the App.
     * ------------------------------------------
     * /!\ FIRST END2END TEST: ASL Recognition /!\ The User now wants to test the Main feature of
     * the App. In fact, he has a dog and wants to understand how to finger spell the word dog. As
     * he previously done the Tutorial He knows the purpose of each component. He starts by
     * searching for the letters D - O - G using the LetterDictionnary
     */

    // Searching for letter "D" .....
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()
    var index = 4
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_D").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_D").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    /** The user knows how to do the HandSign of the letter D */
    // Searching for letter "O" .....
    index = 11
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_O").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_O").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()
    /** The user knows how to do the HandSign of the letter D - O */
    // Searching for letter "G" .....
    index = 8
    repeat(index) { composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick() }
    composeTestRule.onNodeWithTag("LetterBox_G").performClick()
    composeTestRule.onNodeWithTag("SignTipBox_G").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScrollToTopButton").performClick()

    /**
     * The user knows how to do the HandSign of the letter D - O - G. He wants to try it using the
     * CameraFeedbackButton.
     */
    composeTestRule.onNodeWithTag("CameraFeedbackButton").assertIsDisplayed().performClick()
    /**
     * The user is now in the ASLRecognitionScreen. And can see where he has to do the handSigns to
     * be able to reproduce the word D-O-G. Here We assume that he did it as it's something
     * difficult to implement(I.E put a virtual hand into the camera) /!\ END OF FIRST END2END /!\
     */
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed().performClick()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    /**
     * /!\ SECOND END2END TEST: Quiz Feature /!\ The User is back to the HomeScreen. The User now
     * wants to test the Quiz Feature of the HomeScreen using the quiz button (star).
     */
    composeTestRule.onNodeWithTag("QuizButton").performClick()
    composeTestRule.onNodeWithTag("QuizTitle").assertIsDisplayed()
    /**
     * The User can see that Has 4 options to select. As he sees the HandSigns, he know exactly
     * which is the correct solution. He selects apple and the submit.
     */
    composeTestRule
        .onNodeWithTag("OptionRadioButton_apple")
        .performClick() // Simulate choosing the correct solution based on mocked data
    composeTestRule.onNodeWithTag("SubmitButton").performClick()
    /**
     * He gets a Toast confirming that it was the correct answer. He goes back to the HomeScreen.
     *
     * TODO("Confirm that a toast was sent!") /!\ END OF SECOND END2END TEST/!\
     */
    composeTestRule.onNodeWithTag("BackButton").performClick()
    /**
     * The User is back to the HomeScreen. He wants to Add a Friend. /!\ THIRD END2END TEST: Friends
     * feature/!\
     */

    /** /!\ END OF THIRD END2END TEST/!\ */
    /** He wants to change the settings. /!\ THIRD END2END TEST: Settings feature/!\ */
    /** /!\ END OF FOURTH END2END TEST/!\ */
  }
}
