package com.github.se.signify.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class ScreensTest {
  @Test
  fun mainScreenScaffoldDisplaysCorrectInformation() {
    composeTestRule.setContent {
      val navigationActions = mock(NavigationActions::class.java)
      MainScreenScaffold(
          navigationActions = navigationActions,
          testTag = "ScaffoldMainScreen",
          helpTitle = "Help",
          helpText = "This is the help text") {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          }
    }
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScaffoldMainScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun annexScreenScaffoldDisplaysCorrectInformation() {
    val navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      AnnexScreenScaffold(navigationActions = navigationActions, testTag = "ScaffoldAnnexeScreen") {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScaffoldAnnexeScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun screenColumnDisplaysCorrectInformation() {
    composeTestRule.setContent {
      ScreenColumn(
          padding = PaddingValues(16.dp),
          testTag = "ColumnScreen",
      ) {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }
    composeTestRule.onNodeWithTag("ColumnScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @Test
  fun topBarIsDisplayed() {
    composeTestRule.setContent { TopBar() }
    // Assert that the top bar is displayed
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
  }

  @Test
  fun infoPopupIsDisplayed() {
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      InfoPopup(onDismiss = {}, helpTitle = helpTitle, helpText = helpText)
    }
    composeTestRule.onNodeWithTag("InfoPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupTitle").assertTextEquals(helpTitle)
    composeTestRule.onNodeWithTag("InfoPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupBody").assertTextEquals(helpText)
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").assertHasClickAction()
  }

  @Test
  fun infoPopupPerformClickOnButton() {
    var clicked = false
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      InfoPopup(onDismiss = { clicked = true }, helpTitle = helpTitle, helpText = helpText)
    }
    composeTestRule.onNodeWithTag("InfoPopupCloseButton").performClick()
    assert(clicked)
  }

  @Test
  fun backButtonIsDisplayed() {
    composeTestRule.setContent { BackButton(onClick = {}) }
    // Assert that the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
  }

  @Test
  fun backButtonPerformsClick() {
    var clicked = false
    composeTestRule.setContent { BackButton(onClick = { clicked = true }) }
    // Assert that the back button has a click action
    composeTestRule.onNodeWithTag("BackButton").assertHasClickAction()
    // Perform click action on the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()
    // Assert that the click action was triggered
    assert(clicked)
  }
}
