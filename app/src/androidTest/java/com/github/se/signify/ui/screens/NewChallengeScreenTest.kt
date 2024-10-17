import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.Challenge.NewChallengeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class NewChallengeScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent { NewChallengeScreen(navigationActions = navigationActions) }
  }

  @Test
  fun newChallengeScreenDisplaysCorrectElements() {

    // Assert that all expected elements are displayed
    composeTestRule.onNodeWithTag("TopBlueBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NewChallengeContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsChallengeBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsChallengeContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FriendsChallengeTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("CreateChallengeButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeButton1").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeButton2").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ChallengeButton3").assertIsDisplayed()
  }

  @Test
  fun pressingBackArrowNavigatesToChallengeScreen() {

    composeTestRule.onNodeWithTag("BackButton").performClick()

    verify(navigationActions).goBack()
  }

  @Test
  fun pressingMyFriendsButtonNavigatesToFriendsScreen() {

    composeTestRule.onNodeWithTag("MyFriendsButton").performClick()

    verify(navigationActions).navigateTo("Friends")
  }
}
