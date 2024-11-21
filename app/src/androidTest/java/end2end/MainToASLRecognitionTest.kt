package end2end

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.github.se.signify.MainActivity
import org.junit.Rule
import org.junit.Test

class MainToASLRecognitionTest {
  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun navigateToASLRecognitionScreen() {
    composeTestRule.onNodeWithText("Welcome to Signify").assertIsDisplayed()
  }
}
