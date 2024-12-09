package com.github.se.signify.ui.common

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class NotImplementedYetTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun notImplementedYetIsDisplayed() {
    val testTag = "Tag"
    val text = "Nothing for now"
    composeTestRule.setContent { NotImplementedYet(text, testTag) }

    composeTestRule.onNodeWithTag(testTag).onChild().assertIsDisplayed()
    composeTestRule.onNodeWithTag(testTag).onChild().assertTextEquals(text)
  }
}
