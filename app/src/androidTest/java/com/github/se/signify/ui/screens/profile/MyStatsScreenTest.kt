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

  // User information test to be displayed
  private val userId = "userIdTest"
  private val userName = "userNameTest"
  private val profilePictureUrl = "file:///path/to/profile/picture.jpg"
  private val numberOfDays = 10L
  private val lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F')
  private val exercisesAchieved = listOf(10, 3)
  private val questsAchieved = listOf(4, 0)

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)

    composeTestRule.setContent {
      MyStatsScreen(
          userId = userId,
          userName = userName,
          profilePictureUrl = profilePictureUrl,
          navigationActions = navigationActions,
          numberOfDays = numberOfDays,
          lettersLearned = lettersLearned,
          exercisesAchieved = exercisesAchieved,
          questsAchieved = questsAchieved)
    }
  }

  @Test
  fun testMyStatsScreenDisplaysCorrectInformation() {
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()

    // Verify top information are displayed correctly
    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertTextEquals(userId)
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertTextEquals(userName)
    composeTestRule.onNodeWithTag("ProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertTextEquals("$numberOfDays")

    // Verify letters learned section displays correctly
    composeTestRule.onNodeWithTag("AllLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("AllLetterLearned").assertTextEquals("All letters learned")
    composeTestRule.onNodeWithTag("LettersBox").assertIsDisplayed()
    lettersLearned.forEach { letter ->
      composeTestRule.onNodeWithTag(letter.toString()).performScrollTo()
      composeTestRule.onNodeWithText(letter.toString()).assertIsDisplayed()
    }

    // Verify exercises achieved section is displayed with counts
    composeTestRule.onNodeWithTag("ExercisesText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExercisesText").assertTextEquals("Number of exercises achieved")
    composeTestRule.onNodeWithTag("ExercisesEasyCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("EASY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("EASY").assertTextEquals("EASY")
    composeTestRule.onNodeWithTag("${exercisesAchieved[0]}").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("${exercisesAchieved[0]}")
        .assertTextEquals("${exercisesAchieved[0]}")
    composeTestRule.onNodeWithTag("ExercisesHardCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HARD").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HARD").assertTextEquals("HARD")
    composeTestRule.onNodeWithTag("${exercisesAchieved[1]}").assertIsDisplayed()
    composeTestRule
        .onNodeWithTag("${exercisesAchieved[1]}")
        .assertTextEquals("${exercisesAchieved[1]}")

    // Verify quests achieved section is displayed with counts
    composeTestRule.onNodeWithTag("QuestsText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsText").assertTextEquals("Number of quests achieved")
    composeTestRule.onNodeWithTag("DailyQuestCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DAILY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DAILY").assertTextEquals("DAILY")
    composeTestRule.onNodeWithTag("${questsAchieved[0]}").assertIsDisplayed()
    composeTestRule.onNodeWithTag("${questsAchieved[0]}").assertTextEquals("${questsAchieved[0]}")
    composeTestRule.onNodeWithTag("WeeklyQuestsCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WEEKLY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WEEKLY").assertTextEquals("WEEKLY")
    composeTestRule.onNodeWithTag("${questsAchieved[1]}").assertIsDisplayed()
    composeTestRule.onNodeWithTag("${questsAchieved[1]}").assertTextEquals("${questsAchieved[1]}")

    // Verify graph placeholder is displayed
    composeTestRule.onNodeWithTag("GraphsAndStats").assertIsDisplayed()
  }

  @Test
  fun testLettersBoxIsHorizontallyScrollable() {
    // Perform a horizontal scroll action on the letters list
    val scrollableList = composeTestRule.onNodeWithTag("LettersList")

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
