package com.github.se.signify.ui.screens.profile

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class MyStatsScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions

  // User statistics test to be displayed
  private val numberOfDays = 10
  private val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
  private val exercisesAchieved = listOf(10, 3)
  private val questsAchieved = listOf(4, 0)

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    composeTestRule.setContent {
      MyStatsScreen(
          navigationActions = navigationActions,
          numberOfDays = numberOfDays,
          lettersLearned = lettersLearned,
          exercisesAchieved = exercisesAchieved,
          questsAchieved = questsAchieved)
    }
  }

  @Test
  fun testMyStatsScreenDisplaysCorrectInformation() {
    // Verify top blue bar is displayed
    composeTestRule.onNodeWithTag("TopBlueBar").assertIsDisplayed()

    // Verify days count is displayed with correct text
    composeTestRule.onNodeWithTag("flameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("numberOfDays").assertIsDisplayed()
    composeTestRule.onNodeWithTag("numberOfDays").assertTextEquals("10 days")

    // Verify letters learned section displays correctly
    composeTestRule.onNodeWithTag("allLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("allLetterLearned").assertTextEquals("All letters learned")
    composeTestRule.onNodeWithTag("lettersBox").assertIsDisplayed()
    lettersLearned.forEach { letter ->
      composeTestRule.onNodeWithText(letter.toString()).assertIsDisplayed()
    }

    // Verify exercises achieved section is displayed with counts
    composeTestRule.onNodeWithTag("ExercisesText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExercisesText").assertTextEquals("Number of exercises achieved")
    composeTestRule.onNodeWithTag("ExercisesEasyCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Easy").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Easy").assertTextEquals("EASY")
    composeTestRule.onNodeWithTag("ExercisesEasyCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExercisesEasyCount").assertTextEquals("10")
    composeTestRule.onNodeWithTag("ExercisesHardCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Hard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Hard").assertTextEquals("HARD")
    composeTestRule.onNodeWithTag("ExercisesHardCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExercisesHardCount").assertTextEquals("3")

    // Verify quests achieved section is displayed with counts
    composeTestRule.onNodeWithTag("QuestsText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsText").assertTextEquals("Number of quests achieved")
    composeTestRule.onNodeWithTag("DailyQuestCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Daily").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Daily").assertTextEquals("DAILY")
    composeTestRule.onNodeWithTag("DailyQuestCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DailyQuestCount").assertTextEquals("4")
    composeTestRule.onNodeWithTag("WeeklyQuestsCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Weekly").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Weekly").assertTextEquals("WEEKLY")
    composeTestRule.onNodeWithTag("WeeklyQuestsCount").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WeeklyQuestsCount").assertTextEquals("0")

    // Verify graph placeholder is displayed
    composeTestRule.onNodeWithTag("GraphsAndStats").assertIsDisplayed()
  }

  @Test
  fun testLettersBoxIsHorizontallyScrollable() {
    // Perform a horizontal scroll action on the letters list
    val scrollableList = composeTestRule.onNodeWithTag("lettersList")

    // Test scroll from left to right by passing by each letter
    for (letter in 'A'..'Z') {
      // Scroll to the current letter
      scrollableList.performScrollToNode(hasTestTag(letter.toString()))
      // Assert the current letter is displayed
      composeTestRule.onNodeWithTag(letter.toString()).assertIsDisplayed()
    }

    // Test scroll from right to left directly from end to start
    scrollableList.performScrollToNode(hasTestTag("A"))
    // Assert the current letter is displayed
    composeTestRule.onNodeWithTag("A").assertIsDisplayed()
  }

  @Test
  fun testPressingBackArrowNavigatesBack() {
    // Click the back button and verify the action is triggered
    composeTestRule.onNodeWithTag("BackButton").performClick()
    verify(navigationActions).goBack()
  }
}
