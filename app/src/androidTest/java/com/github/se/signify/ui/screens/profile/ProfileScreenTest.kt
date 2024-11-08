package com.github.se.signify.ui.screens.profile

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProfileScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  // User information test to be displayed
  private val userId = "userIdTest"
  private val userName = "userNameTest"
  private val profilePictureUrl = null
  private val numberOfDays = 30
  private val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PROFILE)
    // Initialize the state of isHelpBoxVisible to true
    composeTestRule.setContent {
      ProfileScreen(
          userId = userId,
          userName = userName,
          profilePictureUrl = profilePictureUrl,
          numberOfDays = numberOfDays,
          lettersLearned = lettersLearned,
          navigationActions = navigationActions)
    }
  }

  @Test
  fun buttonsAreCorrectlyDisplayed() {

    composeTestRule.onNodeWithTag("SettingsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyFriendsButton").assertTextEquals("My Friends")
    composeTestRule.onNodeWithTag("MyStatsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MyStatsButton").assertTextEquals("My Stats")
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
    composeTestRule.onNodeWithTag("UserName").assertTextEquals(userName)
    composeTestRule.onNodeWithTag("ProfilePicture").assertIsDisplayed()
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
