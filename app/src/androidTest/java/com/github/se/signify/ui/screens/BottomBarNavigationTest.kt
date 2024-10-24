package com.github.se.signify.ui.screens

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.se.signify.ui.navigation.BottomNavigationMenu
import com.github.se.signify.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class BottomNavigationBarTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun bottomNavigationBarIsDisplayedCorrectly() {
    composeTestRule.setContent {
      BottomNavigationMenu(
          onTabSelect = { navigationActions.navigateTo(it.route) },
          tabList = LIST_TOP_LEVEL_DESTINATION,
          selectedItem = LIST_TOP_LEVEL_DESTINATION.first().route,
          modifier = Modifier.testTag("BottomNavigationBar"))
    }

    composeTestRule.onNodeWithTag("BottomNavigationBar").assertIsDisplayed()
  }
}
