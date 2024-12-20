package com.github.se.signify.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import com.github.se.signify.model.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.model.navigation.NavigationActions
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.never

class ScreensTest {
  @Test
  fun mainScreenScaffoldDisplaysCorrectInformation() {
    composeTestRule.setContent {
      val navigationActions = mock(NavigationActions::class.java)
      MainScreenScaffold(
          navigationActions = navigationActions,
          topLevelDestination = LIST_TOP_LEVEL_DESTINATION.first(),
          testTag = "MainScreenScaffold",
          helpText = HelpText(title = "HelpTitle", content = "HelpText"),
          content = {
            Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
          })
    }

    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule.onNodeWithTag("MainScreenScaffold").assertIsDisplayed()
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
          testTag = "AnnexScreen",
          helpText = HelpText(title = "HelpTitle", content = "HelpText"),
      ) {
        Text(text = "Little text for the column", modifier = Modifier.testTag("Text"))
      }
    }
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("AnnexScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Text").performScrollTo().assertIsDisplayed()
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
  fun helpPopupIsDisplayed() {
    val helpText = HelpText("Help Title", "Help content")
    composeTestRule.setContent { HelpPopup(onDismiss = {}, helpText) }
    composeTestRule.onNodeWithTag("HelpPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertTextEquals(helpText.title)
    composeTestRule.onNodeWithTag("HelpPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupBody").assertTextEquals(helpText.content)
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
          topLevelDestinations = LIST_TOP_LEVEL_DESTINATION,
          selected = LIST_TOP_LEVEL_DESTINATION.first())
    }

    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
  }

  @Test
  fun topBarElementsAreDisplayed() {
    val buttons =
        listOf<@Composable () -> Unit>(
            {
              BasicButton(
                  onClick = {},
                  iconTestTag = "ButtonIcon1",
                  contentDescription = "Button 1",
                  modifier = Modifier.testTag("Button1"),
                  icon = Icons.Outlined.Email)
            },
            {
              BasicButton(
                  onClick = {},
                  iconTestTag = "ButtonIcon2",
                  contentDescription = "Button 2",
                  modifier = Modifier.testTag("Button2"),
                  icon = Icons.Outlined.Email)
            })

    composeTestRule.setContent {
      TopBar(buttons = buttons, helpText = HelpText("Help Title", "Help content"))
    }

    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("TopLine").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Button1").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Button2").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SeparatorLine").assertIsDisplayed()
  }

  @Test
  fun bottomBarDisplaysBottomNavigationMenu() {
    val navigationActions = mock(NavigationActions::class.java)
    composeTestRule.setContent {
      BottomBar(
          navigationActions = navigationActions, selected = LIST_TOP_LEVEL_DESTINATION.first())
    }
    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
  }

  @Test
  fun bottomNavigationMenuNavigates() {
    val navigationActions = mock(NavigationActions::class.java)
    val topLevelDestination = LIST_TOP_LEVEL_DESTINATION.first()
    composeTestRule.setContent {
      BottomNavigationMenu(
          onTabSelect = { navigationActions.navigateTo(it) },
          topLevelDestinations = LIST_TOP_LEVEL_DESTINATION,
          selected = LIST_TOP_LEVEL_DESTINATION[1])
    }

    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("Tab_${topLevelDestination.route}")
        .assertIsDisplayed()
        .performClick()

    // Assert that the click action was never triggered
    verify(navigationActions).navigateTo(topLevelDestination)
  }

  @Test
  fun bottomNavigationMenuDisablesCurrentDestination() {
    val navigationActions = mock(NavigationActions::class.java)
    val topLevelDestination = LIST_TOP_LEVEL_DESTINATION.first()
    composeTestRule.setContent {
      BottomNavigationMenu(
          onTabSelect = { navigationActions.navigateTo(it) },
          topLevelDestinations = LIST_TOP_LEVEL_DESTINATION,
          selected = topLevelDestination)
    }

    composeTestRule.onNodeWithTag("BottomNavigationMenu").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("Tab_${topLevelDestination.route}")
        .assertIsDisplayed()
        .performClick()

    // Assert that the click action was never triggered
    verify(navigationActions, never()).navigateTo(topLevelDestination)
  }

  @Test
  fun helpButtonIsDisplayed() {
    val helpText = HelpText("Help Title", "Help content")
    composeTestRule.setContent { HelpButton(helpText) }
    composeTestRule.onNodeWithTag("HelpButton").assertIsDisplayed()
  }

  @Test
  fun separatorLineIsDisplayed() {
    composeTestRule.setContent { SeparatorLine() }
    composeTestRule.onNodeWithTag("SeparatorLine").assertIsDisplayed()
  }

  @Test
  fun helpButtonClickDisplaysHelpPopup() {
    val helpText = HelpText("Help Title", "Help content")
    composeTestRule.setContent { HelpButton(helpText) }
    composeTestRule.onNodeWithTag("HelpButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("HelpButton").performClick()

    // Assert that the help popup and its contents are displayed
    composeTestRule.onNodeWithTag("HelpPopup").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupContent").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupTitle").assertTextEquals(helpText.title)
    composeTestRule.onNodeWithTag("HelpPopupBody").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HelpPopupBody").assertTextEquals(helpText.content)
  }
}
