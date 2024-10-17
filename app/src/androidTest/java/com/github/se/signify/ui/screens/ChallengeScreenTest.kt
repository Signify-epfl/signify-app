import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.Challenge.ChallengeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ChallengeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun challengeScreenDisplaysCorrectElements() {
    composeTestRule.setContent { ChallengeScreen(navigationActions = navigationActions) }

    // Assert that all elements are displayed in ChallengeScreen
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeScreenContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("TopBlueLine").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ButtonsColumn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HistoryButton").assertIsDisplayed()

    // Click on the InfoButton to show the InfoPopup
    composeTestRule.onNodeWithTag("InfoButton").performClick()

    // Assert that all elements within the InfoPopup are displayed after clicking the InfoButton
    composeTestRule.onNodeWithTag("InfoPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertIsDisplayed()
  }

  @Test
  fun pressingHistoryButtonNavigatesToChallengeHistoryScreen() {
    composeTestRule.setContent { ChallengeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("HistoryButton").performClick()

    verify(navigationActions).navigateTo("ChallengeHistory")
  }

  @Test
  fun pressingChallengeButtonNavigatesToNewChallengeScreen() {
    composeTestRule.setContent { ChallengeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("ChallengeButton").performClick()

    verify(navigationActions).navigateTo("NewChallenge")
  }

  @Test
  fun pressingInfoButtonShowsInfoPopup() {
    composeTestRule.setContent { ChallengeScreen(navigationActions = navigationActions) }

    composeTestRule.onNodeWithTag("InfoButton").performClick()

    composeTestRule.onNodeWithTag("InfoPopup").assertIsDisplayed()
  }
}