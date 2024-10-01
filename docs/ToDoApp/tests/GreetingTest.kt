package com.github.se.bootcamp.ui

// ***************************************************************************** //
// ***                                                                       *** //
// *** THIS FILE WILL BE OVERWRITTEN DURING GRADING. IT SHOULD BE LOCATED IN *** //
// *** `app/src/androidTest/java/com/github/se/bootcamp/`.                   *** //
// *** DO **NOT** IMPLEMENT YOUR OWN TESTS IN THIS FILE                      *** //
// ***                                                                       *** //
// ***************************************************************************** //

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun everythingIsDisplayed() {
    composeTestRule.setContent { Greeting() }

    composeTestRule.onNodeWithTag("greetingNameInput").assertIsDisplayed()
    composeTestRule.onNodeWithTag("greetingButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("greetingDisplay").assertIsDisplayed()
  }

  @Test
  fun displayHasCorrectDefaultValue() {
    composeTestRule.setContent { Greeting() }

    composeTestRule.onNodeWithTag("greetingDisplay").assertTextEquals("What's your name ?")
  }

  @Test
  fun displayCorrectlyUpdates() {
    composeTestRule.setContent { Greeting() }

    composeTestRule.onNodeWithTag("greetingNameInput").performTextInput("John Doe")
    composeTestRule.onNodeWithTag("greetingButton").performClick()
    composeTestRule.onNodeWithTag("greetingDisplay").assertTextEquals("Hi John Doe !")
  }
}