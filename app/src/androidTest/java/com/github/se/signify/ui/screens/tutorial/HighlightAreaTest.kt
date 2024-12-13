package com.github.se.signify.ui.screens.tutorial

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class HighlightAreaTest {

  @get:Rule val composeTestRule = createComposeRule()

  private val elementPosition =
      ElementPosition(x = 50.dp, y = 100.dp, width = 200.dp, height = 150.dp)

  @Test
  fun testFullScreenSemiTransparentOverlay_isDisplayed() {
    composeTestRule.setContent { FullScreenSemiTransparentOverlay(overlayColor = Color.Black) }

    // Verify that the overlay is displayed
    composeTestRule.onRoot().assertIsDisplayed()
    composeTestRule.onNodeWithTag("FullScreenOverlay").isDisplayed()
  }

  @Test
  fun testHighlightedBox_displaysCorrectOverlays() {

    composeTestRule.setContent {
      HighlightedBox(elementPosition = elementPosition, overlayColor = Color.Red)
    }

    // Verify that the root exists (overlay is rendered)
    composeTestRule.onRoot().assertExists()

    // Verify that overlays are displayed correctly
    composeTestRule.onNodeWithTag("TopOverlay").assertExists()
    composeTestRule.onNodeWithTag("LeftOverlay").assertExists()
    composeTestRule.onNodeWithTag("RightOverlay").assertExists()
    composeTestRule.onNodeWithTag("BottomOverlay").assertExists()

    composeTestRule.onNodeWithTag("HighlightedBox").assertExists()
  }

  @Test
  fun testHighlightArea_displaysCorrectContent_forBox() {

    composeTestRule.setContent {
      HighlightArea(
          HighlightedArea.HighlightedBoxArea(
              elementPosition = elementPosition, overlayColor = Color.Gray))
    }

    // Verify the HighlightArea is rendered correctly as HighlightedBox
    composeTestRule.onRoot().assertExists()

    // Verify that overlays are displayed correctly
    composeTestRule.onNodeWithTag("TopOverlay").assertExists()
    composeTestRule.onNodeWithTag("LeftOverlay").assertExists()
    composeTestRule.onNodeWithTag("RightOverlay").assertExists()
    composeTestRule.onNodeWithTag("BottomOverlay").assertExists()

    composeTestRule.onNodeWithTag("HighlightedBox").assertExists()
  }

  @Test
  fun testHighlightArea_displaysCorrectContent_forFullScreen() {
    composeTestRule.setContent {
      HighlightArea(HighlightedArea.FullScreenOverlayArea(overlayColor = Color.Blue))
    }

    // Verify the HighlightArea is rendered correctly as FullScreenOverlay
    composeTestRule.onRoot().assertExists()
    composeTestRule.onNodeWithTag("FullScreenOverlay").isDisplayed()
  }
}
