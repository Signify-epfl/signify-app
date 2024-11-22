package com.github.se.signify.ui.screens.profile

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class MyStatsScreenTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var navigationActions: NavigationActions
  private lateinit var userSession: UserSession
  private lateinit var userRepository: UserRepository
  private lateinit var statsRepository: StatsRepository

  @Before
  fun setUp() {
    navigationActions = mock(NavigationActions::class.java)
    userSession = MockUserSession()
    userRepository = mock(UserRepository::class.java)
    statsRepository = mock(StatsRepository::class.java)

    composeTestRule.setContent {
      MyStatsScreen(
          navigationActions = navigationActions,
          userSession = userSession,
          userRepository = userRepository,
          statsRepository = statsRepository)
    }
  }

  @Test
  fun testMyStatsScreenDisplaysCorrectInformation() {
    composeTestRule.onNodeWithTag("TopBar").assertIsDisplayed()

    // Verify top information are displayed correctly
    composeTestRule.onNodeWithTag("UserInfo").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserId").assertIsDisplayed()
    composeTestRule.onNodeWithTag("UserName").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DefaultProfilePicture").assertExists()
    composeTestRule.onNodeWithTag("StreakCounter").assertIsDisplayed()
    composeTestRule.onNodeWithTag("FlameIcon").assertIsDisplayed()
    composeTestRule.onNodeWithTag("NumberOfDays").assertIsDisplayed()

    // Verify letters learned section displays correctly
    composeTestRule.onNodeWithTag("AllLetterLearned").assertIsDisplayed()
    composeTestRule.onNodeWithTag("AllLetterLearned").assertTextEquals("All letters learned")
    composeTestRule.onNodeWithTag("LettersBox").assertIsDisplayed()

    // Verify exercises achieved section is displayed with counts
    composeTestRule.onNodeWithTag("ExercisesText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ExercisesText").assertTextEquals("Number of exercises achieved")
    composeTestRule.onNodeWithTag("ExercisesEasyCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("EASY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("EASY").assertTextEquals("EASY")
    composeTestRule.onNodeWithTag("ExercisesHardCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HARD").assertIsDisplayed()
    composeTestRule.onNodeWithTag("HARD").assertTextEquals("HARD")

    // Verify quests achieved section is displayed with counts
    composeTestRule.onNodeWithTag("QuestsText").assertIsDisplayed()
    composeTestRule.onNodeWithTag("QuestsText").assertTextEquals("Number of quests achieved")
    composeTestRule.onNodeWithTag("DailyQuestCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DAILY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("DAILY").assertTextEquals("DAILY")
    composeTestRule.onNodeWithTag("WeeklyQuestsCountBox").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WEEKLY").assertIsDisplayed()
    composeTestRule.onNodeWithTag("WEEKLY").assertTextEquals("WEEKLY")

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
