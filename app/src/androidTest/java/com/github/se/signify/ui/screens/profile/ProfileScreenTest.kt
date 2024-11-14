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
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.ProfilePicture
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
  private lateinit var userRepository: UserRepository
  private lateinit var userViewModel: UserViewModel

  // User information test to be displayed
  private val userId =
      FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0) ?: "unknown"
  private val numberOfDays = 30

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userRepository = mock(UserRepository::class.java)
    userViewModel = UserViewModel(userRepository)
    val picturePath = "file:///path/to/profile/picture.jpg"

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PROFILE)
    // Initialize the state of isHelpBoxVisible to true
    composeTestRule.setContent {
      ProfileScreen(navigationActions, userRepository)
      ProfilePicture(picturePath)
    }
  }

  @Test
  fun buttonsAreCorrectlyDisplayed() {

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
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("$numberOfDays days")
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
