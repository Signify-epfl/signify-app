package com.github.se.signify.ui.screens.profile

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProfileScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var statsRepository: StatsRepository
  private lateinit var userViewModel: UserViewModel
  private lateinit var statsViewModel: StatsViewModel

  // User information test to be displayed
  private val userId =
      FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown"

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)
    statsRepository = mock(StatsRepository::class.java)
    userViewModel = UserViewModel(userSession, userRepository)
    statsViewModel = StatsViewModel(userSession, statsRepository)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PROFILE.route)
    composeTestRule.setContent {
      ProfileScreen(navigationActions, userSession, userRepository, statsRepository)
    }
  }

  @Test
  fun buttonsAreCorrectlyDisplayed() {

    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SettingsButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsButton").performScrollTo().assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("MyFriendsButton")
        .performScrollTo()
        .assertTextEquals("My Friends")
    composeTestRule.onNodeWithTag("MyStatsButton").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyStatsButton").performScrollTo().assertTextEquals("My Stats")
  }

  @Test
  fun buttonsHaveClickAction() {

    composeTestRule.onNodeWithTag("SettingsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("InfoButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("MyFriendsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("MyStatsButton").assertHasClickAction()
  }

  @Test
  fun buttonsPerformClick() {

    composeTestRule.onNodeWithTag("SettingsButton").performClick()
    composeTestRule.onNodeWithTag("InfoButton").performClick()
    composeTestRule.onNodeWithTag("MyFriendsButton").performClick()
    composeTestRule.onNodeWithTag("MyStatsButton").performClick()
  }

  @Test
  fun userInfoAreDisplayed() {

    // Verify top information are displayed correctly
    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertTextEquals(userId)
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertTextEquals(userViewModel.userName.value)
    composeTestRule.onNodeWithTag("DefaultProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertIsDisplayed()
  }

  @Test
  fun letterListScrolls() {

    // Perform a horizontal scroll action on the letters list
    val scrollableList = composeTestRule.onNodeWithTag("LettersList")

    // Test scroll from left to right by passing by each letter
    for (letter in 'A'..'Z') {
      // Scroll to the current letter
      scrollableList.performScrollToNode(hasTestTag(letter.toString()))
      // Assert the current letter is displayed
      composeTestRule.onNodeWithTag(letter.toString()).assertIsDisplayed()
    }

    // Test scroll from right to left directly from end to start
    scrollableList.performScrollToNode(hasTestTag("A"))
    // Assert the current letter is displayed
    composeTestRule.onNodeWithTag("A").assertIsDisplayed()
  }

  @Test
  fun dialogIsDisplayedWhenHelpBoxIsVisible() {

    // Assert that the help dialog is displayed when the help button is clicked
    composeTestRule.onNodeWithTag("InfoButton").performClick()
    composeTestRule.onNodeWithTag("InfoPopupContent").assertIsDisplayed()
  }

  @Test
  fun dialogClosesWhenCloseButtonClicked() {

    // Click the 'Close' button in the dialog
    composeTestRule.onNodeWithTag("InfoButton").performClick()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").performClick()

    // After clicking, the dialog should no longer be displayed
    composeTestRule.onNodeWithTag("InfoPopupContent").assertDoesNotExist()
  }
}
