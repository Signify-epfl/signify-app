package com.github.se.signify.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.PROFILE)
  }

  @Test
  fun buttonsAreCorrectlyDisplayed() {

    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    composeTestRule.onNodeWithTag("settingsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("helpButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("My FriendsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("My FriendsButton").assertTextEquals("My Friends")
    composeTestRule.onNodeWithTag("My StatsButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("My StatsButton").assertTextEquals("My Stats")
  }

  @Test
  fun buttonsHaveClickAction() {

    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    composeTestRule.onNodeWithTag("settingsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("helpButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("My FriendsButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("My StatsButton").assertHasClickAction()
  }

  @Test
  fun buttonsPerformClick() {
    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    composeTestRule.onNodeWithTag("settingsButton").performClick()
    composeTestRule.onNodeWithTag("helpButton").performClick()
    composeTestRule.onNodeWithTag("My FriendsButton").performClick()
    composeTestRule.onNodeWithTag("My StatsButton").performClick()
  }

  @Test
  fun userInfoAreDisplayed() {
    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    composeTestRule.onNodeWithTag("userInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("flameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("profilePicture").assertIsDisplayed()
    composeTestRule.onNodeWithTag("lettersBox").assertIsDisplayed()
  }

  @Test
  fun letterListScrolls() {
    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    composeTestRule.onNodeWithTag("lettersList").performScrollTo()
  }

  @Test
  fun dialogIsDisplayed_whenHelpBoxIsVisible() {
    // Initialize the state of isHelpBoxVisible to true
    composeTestRule.setContent {
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    // Assert that the help dialog is displayed when the help button is clicked
    composeTestRule.onNodeWithTag("helpButton").performClick()
    composeTestRule.onNodeWithTag("helpProfileText").assertIsDisplayed()
  }

  @Test
  fun dialogCloses_whenCloseButtonClicked() {
    // Initialize the state of isHelpBoxVisible to true
    composeTestRule.setContent {
      val isHelpBoxVisible = remember { mutableStateOf(true) }
      ProfileScreen(
          userId = "Test ID 1",
          userName = "Test Name 1",
          profilePictureUrl = null, // Replace with actual URL or null
          numberOfDays = 30,
          lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
          navigationActions)
    }

    // Click the 'Close' button in the dialog
    composeTestRule.onNodeWithTag("helpButton").performClick()
    composeTestRule.onNodeWithTag("closeButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("closeButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("closeButton").performClick()

    // After clicking, the dialog should no longer be displayed
    composeTestRule.onNodeWithTag("helpProfileText").assertDoesNotExist()
  }
}
