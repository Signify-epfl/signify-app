package com.github.se.signify.ui.screens

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.home.ExerciseScreenHard
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class ExerciseScreenHardTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var mockNavigationActions: NavigationActions

  @Before
  fun setup() {
    mockNavigationActions = mock(NavigationActions::class.java)
  }

  @Test
  fun exerciseScreenHard_displaysComponentsCorrectly() {
    composeTestRule.setContent { ExerciseScreenHard(navigationActions = mockNavigationActions) }

    // Verify if the back button is displayed
    composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

    // Check if the formatted word is displayed correctly

    var expectedWordDisplay =
        "Fgyffg" // FGYFFG first word, F is the current letter, and the others are lowercased
    composeTestRule.onNodeWithTag("FirstWordTag").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FirstWordTag").assertTextEquals(expectedWordDisplay)

    expectedWordDisplay = "fgygfg" // FGYGFG first word, lowercased
    composeTestRule.onNodeWithTag("SecondWordTag").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SecondWordTag").assertTextEquals(expectedWordDisplay)

    // Check if the success button is displayed and clickable
    composeTestRule.onNodeWithText("Success").assertIsDisplayed().assertHasClickAction()
  }

  @Test
  fun successButton_incrementsLetterIndexOrShowsToast() {
    // Set the screen content for testing
    composeTestRule.setContent { ExerciseScreenHard(navigationActions = mockNavigationActions) }

    // Check the initial word display (before clicking the success button)
    val initialWordDisplay = "Fgyffg"
    composeTestRule.onNodeWithTag("FirstWordTag").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FirstWordTag").assertTextEquals(initialWordDisplay)

    // Click on the success button
    composeTestRule.onNodeWithText("Success").performClick()

    // After clicking, the word display should update to the next letter or show a toast
    // In this example, we assume it moves to the next letter; you can update this based on behavior
    val nextLetterDisplay = "fGyffg" // Assuming it moves to the next letter
    composeTestRule.onNodeWithTag("FirstWordTag").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FirstWordTag").assertTextEquals(nextLetterDisplay)

    // Test moving from one word to the other aswell, test the second word when first changed,...

  }
}
