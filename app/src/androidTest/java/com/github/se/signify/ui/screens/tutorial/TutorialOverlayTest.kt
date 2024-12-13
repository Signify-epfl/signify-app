package com.github.se.signify.ui.screens.tutorial

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TutorialOverlayTest {

  @get:Rule val composeTestRule = createComposeRule()

  private var onNextClicked = false

  @Before
  fun setUp() {
    onNextClicked = false
  }

  @Test
  fun testTutorialOverlay_displaysTextAndButton() {
    composeTestRule.setContent {
      TutorialOverlay(
          text = "Welcome to the tutorial",
          highlightArea = HighlightedArea.FullScreenOverlayArea(overlayColor = Color.Black),
          onNext = { onNextClicked = true },
          textTag = "WelcomeText")
    }

    // Assert that the text is displayed
    composeTestRule.onNodeWithTag("WelcomeText").assertIsDisplayed()

    // Assert that the "Next" button is displayed
    composeTestRule.onNodeWithTag("nextButton").assertIsDisplayed()
  }

  @Test
  fun testTutorialOverlay_highlightAreaIsDisplayed() {
    composeTestRule.setContent {
      TutorialOverlay(
          text = "Highlighting an area",
          highlightArea =
              HighlightedArea.HighlightedBoxArea(
                  elementPosition =
                      ElementPosition(x = 50.dp, y = 100.dp, width = 200.dp, height = 150.dp),
                  overlayColor = Color.Gray),
          onNext = { onNextClicked = true },
          textTag = "HighlightText")
    }

    // Assert that the highlight area is rendered
    composeTestRule.onNodeWithTag("HighlightArea").assertIsDisplayed()
  }

  @Test
  fun testTutorialOverlay_onNextButtonClick() {
    composeTestRule.setContent {
      TutorialOverlay(
          text = "Click Next",
          highlightArea = HighlightedArea.FullScreenOverlayArea(overlayColor = Color.Black),
          onNext = { onNextClicked = true },
          textTag = "ClickNextText")
    }

    // Click the "Next" button
    composeTestRule.onNodeWithTag("nextButton").performClick()

    // Verify that the onNext callback was triggered
    assertTrue("onNext should be triggered", onNextClicked)
  }
}
