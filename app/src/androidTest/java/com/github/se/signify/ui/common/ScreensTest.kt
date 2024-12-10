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
import com.github.se.signify.model.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ScreensTest {
  @Test
  fun mainScreenScaffoldDisplaysCorrectInformation() {
    composeTestRule.setContent {
      val navigationActions = mock(NavigationActions::class.java)
      MainScreenScaffold(
          navigationActions = navigationActions,
          testTag = "ScaffoldMainScreen",
          helpText = HelpText(title = "HelpTitle", text = "HelpText"),
          content = {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          })
    }
    composeTestRule.onNodeWithTag("TopLine").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ScaffoldMainScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").assertIsDisplayed()
  }

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun annexScreenScaffoldDisplaysCorrectInformation() {
    val navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      AnnexScreenScaffold(
          navigationActions = navigationActions,
          testTag = "ScaffoldAnnexeScreen",
      ) {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }
    composeTestRule.onNodeWithTag("TopLine").assertIsDisplayed()
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
  fun topLineIsDisplayed() {
    composeTestRule.setContent { TopLine() }
    // Assert that the top bar is displayed
    composeTestRule.onNodeWithTag("TopLine").assertIsDisplayed()
  }

  @Test
  fun infoPopupIsDisplayed() {
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      HelpPopup(onDismiss = {}, HelpText(title = helpTitle, text = helpText))
    }
    composeTestRule.onNodeWithTag("HelpPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertTextEquals(helpTitle)
    composeTestRule.onNodeWithTag("HelpPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupBody").assertTextEquals(helpText)
    composeTestRule.onNodeWithTag("HelpPopupCloseButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupCloseButton").assertHasClickAction()
  }

  @Test
  fun infoPopupPerformClickOnButton() {
    var clicked = false
    val helpTitle = "Help"
    val helpText = "Little text for the help"
    composeTestRule.setContent {
      HelpPopup(onDismiss = { clicked = true }, HelpText(title = helpTitle, text = helpText))
    }
    composeTestRule.onNodeWithTag("HelpPopupCloseButton").performClick()
    assert(clicked)
  }

  @Test
  fun backButtonIsDisplayed() {
    composeTestRule.setContent { BackButton(mock(NavigationActions::class.java)) }
    // Assert that the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
  }

  @Test
  fun backButtonPerformsClick() {
    val navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent { BackButton(navigationActions) }
    // Assert that the back button has a click action
    composeTestRule.onNodeWithTag("BackButton").assertHasClickAction()
    // Perform click action on the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()
    // Assert that the click action was triggered
    verify(navigationActions).goBack()
  }

  @Test
  fun bottomNavigationBarIsDisplayedCorrectly() {
    val navigationActions = mock(NavigationActions::class.java)

    composeTestRule.setContent {
      BottomNavigationMenu(
          onTabSelect = { navigationActions.navigateTo(it) },
          tabList = LIST_TOP_LEVEL_DESTINATION,
          selectedItem = LIST_TOP_LEVEL_DESTINATION.first().route)
    }

    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
  }
}
