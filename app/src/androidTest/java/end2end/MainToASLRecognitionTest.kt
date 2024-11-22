package end2end

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.signify.MainActivity
import org.junit.Rule
import org.junit.Test

class MainToASLRecognitionTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()
  /*
   private lateinit var firebaseAuth: FirebaseAuth

   @Before
   fun setup() {
     // Initialize Firebase for testing
     FirebaseApp.initializeApp(composeTestRule.activity)
     firebaseAuth = Firebase.auth

     // Use Firebase Emulator Suite for testing
     firebaseAuth.useEmulator("10.0.2.2", 9099) // 10.0.2.2 for Android emulator

     // Create a stub Google sign-in user
     firebaseAuth.signInAnonymously()
       .addOnCompleteListener { task ->
         if (!task.isSuccessful) {
           throw RuntimeException("Failed to create test user: ${task.exception}")
         }
       }
   }

  */

  @Test
  fun navigateToASLRecognitionScreen() {
    // Assert Welcome Screen is displayed
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()

    // Wait for the transition to Login Screen
    composeTestRule.mainClock.advanceTimeBy(7_000)
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()

    // Simulate the Google sign-in process
    composeTestRule.onNodeWithTag("offlineButton").performClick()

    // Wait for navigation to Home Screen
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()
    // The "user" searches for the letters d - o - g so that he can navigate to the ASL Recognition
    // Screen

  }
}
