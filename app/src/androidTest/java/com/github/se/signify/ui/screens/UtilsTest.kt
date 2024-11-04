package com.github.se.signify.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
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
          testTag = textTag,
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
          testTag = textTag,
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

  @Test
  fun squareButton_isDisplayed_andClickable() {
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconRes = R.drawable.battleicon,
          label = label,
          onClick = {},
          size = 100.dp,
          iconSize = 50.dp,
          labelFontSize = 16.sp,
          iconTint = Color.Gray,
          textColor = Color.White,
          modifier = Modifier.testTag(label))
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithText(label).assertIsDisplayed()

    // Assert that the button has a click action
    composeTestRule.onNodeWithText(label).assertHasClickAction()
  }

  @Test
  fun squareButton_performsClick() {
    var clicked = false
    val label = "Test Button"
    composeTestRule.setContent {
      SquareButton(
          iconRes = R.drawable.battleicon,
          label = label,
          onClick = { clicked = true },
          size = 100.dp,
          iconSize = 50.dp,
          labelFontSize = 16.sp,
          iconTint = Color.Gray,
          textColor = Color.White,
          modifier = Modifier.testTag(label))
    }

    // Perform click action on the button
    composeTestRule.onNodeWithText(label).performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  // Test for UtilButton
  @Test
  fun utilButton_isDisplayed_andClickable() {
    composeTestRule.setContent {
      UtilButton(
          onClick = {},
          testTagButton = "UtilButton",
          testTagIcon = "UtilIcon",
          icon = Icons.Outlined.Info,
          contentDescription = "Info")
    }

    // Assert that the button is displayed
    composeTestRule.onNodeWithTag("UtilButton").assertIsDisplayed()

    // Assert that the button has a click action
    composeTestRule.onNodeWithTag("UtilButton").assertHasClickAction()
  }

  @Test
  fun utilButton_performsClick() {
    var clicked = false
    composeTestRule.setContent {
      UtilButton(
          onClick = { clicked = true },
          testTagButton = "UtilButton",
          testTagIcon = "UtilIcon",
          icon = Icons.Outlined.Info,
          contentDescription = "Info")
    }

    // Perform click action on the button
    composeTestRule.onNodeWithTag("UtilButton").performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }

  // Test for BackButton
  @Test
  fun backButton_isDisplayed_andClickable() {
    composeTestRule.setContent { BackButton(onClick = {}) }

    // Assert that the back button is displayed
    composeTestRule.onNodeWithTag("BackButton").assertIsDisplayed()

    // Assert that the back button has a click action
    composeTestRule.onNodeWithTag("BackButton").assertHasClickAction()
  }

  @Test
  fun backButton_performsClick() {
    var clicked = false
    composeTestRule.setContent { BackButton(onClick = { clicked = true }) }

    // Perform click action on the back button
    composeTestRule.onNodeWithTag("BackButton").performClick()

    // Assert that the click action was triggered
    assert(clicked)
  }
}
