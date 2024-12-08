package com.github.se.signify.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.github.se.signify.R
import org.junit.Rule
import org.junit.Test

class ButtonsTest {
  @Test
  fun basicButtonIsDisplayed() {
    composeTestRule.setContent {
      BasicButton(
          onClick = {},
          icon = Icons.Outlined.Info,
          iconTestTag = "UtilIcon",
          contentDescription = "Info",
          modifier = Modifier.testTag("UtilButton"),
      )
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithTag("UtilButton").assertIsDisplayed()
  }

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun basicButtonPerformsClick() {
    var clicked = false
    composeTestRule.setContent {
      BasicButton(
          onClick = { clicked = true },
          icon = Icons.Outlined.Info,
          iconTestTag = "UtilIcon",
          contentDescription = "Info",
          modifier = Modifier.testTag("UtilButton"))
    }

    // Assert that the button has a click action
    composeTestRule.onNodeWithTag("UtilButton").assertHasClickAction()

    // Perform click action on the button
    composeTestRule.onNodeWithTag("UtilButton").performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  @Test
  fun textButtonDisplaysCorrectly() {
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test
    composeTestRule.setContent {
      TextButton(
          onClick = {},
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
          textColor = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier)
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(textTag).assertIsDisplayed()

    // Assert the button contains the correct text
    composeTestRule.onNodeWithText(buttonText).assertExists()
  }

  @Test
  fun textButtonPerformsClick() {
    var clickCounter = 0
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      TextButton(
          onClick = { clickCounter++ },
          testTag = textTag,
          text = buttonText,
          backgroundColor = Color.Blue,
          textColor = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier)
    }

    // Perform a click action on the button
    composeTestRule.onNodeWithTag(textTag).performClick()

    // Assert the click listener was triggered
    assert(clickCounter == 1)
  }

  @Test
  fun squareButtonIsDisplayed() {
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconId = R.drawable.battleicon,
          onClick = {},
          text = label,
          size = 100,
          modifier = Modifier.testTag(label),
      )
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithText(label).assertIsDisplayed()
  }

  @Test
  fun squareButtonPerformsClick() {
    var clicked = false
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconId = R.drawable.battleicon,
          onClick = { clicked = true },
          text = label,
          size = 100,
          modifier = Modifier.testTag(label),
      )
    }

    // Assert that the button has a click action
    composeTestRule.onNodeWithText(label).assertHasClickAction()

    // Perform click action on the button
    composeTestRule.onNodeWithText(label).performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }
}
