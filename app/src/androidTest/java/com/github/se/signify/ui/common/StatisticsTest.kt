package com.github.se.signify.ui.common

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import org.junit.Rule
import org.junit.Test

class StatisticsTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun statisticsRowIsDisplayed() {
    val columnTestTag = "StatsColumn"
    val rowTestTag = "StatsRow"
    val lineText = "Line stats text"
    val lineTextTag = "LineTag"
    val statsTextList = listOf("test1", "test2")
    val statsNumberList = listOf("1", "2")
    composeTestRule.setContent {
      StatisticsTable(
          lineText = lineText,
          statsTexts = statsTextList,
          statsNumberList = statsNumberList,
          columnTestTag = columnTestTag,
          rowTestTag = rowTestTag,
          lineTextTestTag = lineTextTag)
    }
    composeTestRule.onNodeWithTag(columnTestTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(rowTestTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertIsDisplayed()
    composeTestRule.onNodeWithTag(lineTextTag).assertTextEquals(lineText)
    statsTextList.forEach { statsText ->
      composeTestRule.onNodeWithTag(statsText).assertIsDisplayed().assertTextEquals(statsText)
    }
    statsNumberList.forEach { statsNumber ->
      composeTestRule.onNodeWithTag(statsNumber).assertIsDisplayed().assertTextEquals(statsNumber)
    }
  }

  @Test
  fun horizontalLetterListIsDisplayedAndScrollable() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { LetterList(lettersLearned) }
    composeTestRule.onNodeWithTag("LettersList").assertIsDisplayed()
    val scrollableList = composeTestRule.onNodeWithTag("LettersList")
    for (letter in 'A'..'Z') {
      scrollableList.performScrollToNode(hasTestTag(letter.toString()))
      composeTestRule.onNodeWithTag(letter.toString()).assertIsDisplayed()
    }
    scrollableList.performScrollToNode(hasTestTag("A"))
    composeTestRule.onNodeWithTag("A").assertIsDisplayed()
  }

  @Test
  fun learnedLetterListIsDisplayed() {
    val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
    composeTestRule.setContent { LearnedLetterList(lettersLearned) }
    composeTestRule.onNodeWithTag("AllLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("LettersList").assertIsDisplayed()
  }

  @Test
  fun streakCounterIsDisplayed() {
    composeTestRule.setContent { StreakCounter(10) }
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("10")
  }
}
