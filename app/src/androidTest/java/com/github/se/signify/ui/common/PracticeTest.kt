package com.github.se.signify.ui.common

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class PracticeTest {
  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    `when`(navigationActions.currentRoute()).thenReturn(Screen.HOME.route)
  }

  @Test
  fun initialLetterIsDisplayedCorrectly() {
    lateinit var coroutineScope: CoroutineScope
    composeTestRule.setContent {
      coroutineScope = rememberCoroutineScope()
      LetterDictionary(coroutineScope = coroutineScope, numbOfHeaders = 0, clickable = false)
    }
    composeTestRule.onNodeWithTag("LetterBox_A").assertIsDisplayed()
  }

  @Test
  fun forwardArrowNavigatesThroughAllLettersCorrectly() {
    composeTestRule.setContent {
      val coroutineScope = rememberCoroutineScope()
      LetterDictionary(coroutineScope = coroutineScope, numbOfHeaders = 0, clickable = false)
    }

    val letters = ('A'..'Z').toList()

    letters.forEach { letter ->
      // Assert that the letter and corresponding icon are displayed
      composeTestRule.onNodeWithTag("LetterBox_$letter").assertIsDisplayed()

      // Click on the forward arrow to go to the next letter
      composeTestRule.onNodeWithTag("LetterDictionaryForward").performClick()
    }

    // After looping through all letters, it should go back to 'A'
    composeTestRule.onNodeWithTag("LetterBox_A").assertIsDisplayed()
  }

  @Test
  fun backArrowNavigatesThroughAllLettersCorrectly() {
    composeTestRule.setContent {
      val coroutineScope = rememberCoroutineScope()
      LetterDictionary(coroutineScope = coroutineScope, numbOfHeaders = 0, clickable = false)
    }

    val letters = ('A'..'Z').reversed().toList()

    // Click the back arrow initially to move to 'Z'
    composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()

    letters.forEach { letter ->
      // Assert that the letter and corresponding icon are displayed
      composeTestRule.onNodeWithTag("LetterBox_$letter").assertIsDisplayed()

      // Click on the back arrow to go to the previous letter
      composeTestRule.onNodeWithTag("LetterDictionaryBack").performClick()
    }

    // After looping through all letters, it should go back to 'Z'
    composeTestRule.onNodeWithTag("LetterBox_Z").assertIsDisplayed()
  }
}
