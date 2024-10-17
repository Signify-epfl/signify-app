package com.github.se.signify.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.junit.Rule
import org.junit.Test

class UtilsTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun reusableButtonWithIcon_isDisplayed_andClickable() {
    val iconDescription = "Info"

    // Set the content for the test
    composeTestRule.setContent {
      ReusableButtonWithIcon(
          onClickAction = {}, icon = Icons.Outlined.Info, iconDescription = iconDescription)
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(iconDescription + "Button").assertIsDisplayed()

    // Assert the button has a click action
    composeTestRule.onNodeWithTag(iconDescription + "Button").assertHasClickAction()
  }

  @Test
  fun reusableButtonWithIcon_performsClick() {
    var clickCounter = 0
    val iconDescription = "Info"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      ReusableButtonWithIcon(
          onClickAction = { clickCounter++ },
          icon = Icons.Outlined.Info,
          iconDescription = iconDescription)
    }

    // Perform a click action on the button
    composeTestRule.onNodeWithTag(iconDescription + "Button").performClick()

    // Assert the click listener was triggered
    assert(clickCounter == 1)
  }

  @Test
  fun reusableTextButton_isDisplayed_withCorrectText() {
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test
    composeTestRule.setContent {
      ReusableTextButton(
          onClickAction = {},
          textTag = textTag,
          text = buttonText,
          height = 48.dp,
          borderColor = Color.Black,
          backgroundColor = Color.Blue,
          textSize = 16.sp,
          textColor = Color.White)
    }

    // Assert the button is displayed
    composeTestRule.onNodeWithTag(textTag).assertIsDisplayed()

    // Assert the button contains the correct text
    composeTestRule.onNodeWithText(buttonText).assertExists()
  }

  @Test
  fun reusableTextButton_performsClick() {
    var clickCounter = 0
    val textTag = "TestButton"
    val buttonText = "Click Me"

    // Set the content for the test with a click listener that increments the counter
    composeTestRule.setContent {
      ReusableTextButton(
          onClickAction = { clickCounter++ },
          textTag = textTag,
          text = buttonText,
          height = 48.dp,
          borderColor = Color.Black,
          backgroundColor = Color.Blue,
          textSize = 16.sp,
          textColor = Color.White)
    }

    // Perform a click action on the button
    composeTestRule.onNodeWithTag(textTag).performClick()

    // Assert the click listener was triggered
    assert(clickCounter == 1)
  }
}
