package com.github.se.signify.ui.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.se.signify.SignifyAppPreview
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import com.github.se.signify.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class MainActivityTest {
  @get:Rule val composeTestRule = createComposeRule()
  val navigationState = MutableStateFlow<NavigationActions?>(null)

  @Test
  fun testNavigationFromMainActivityAndEveryScreenIsDisplayed() {
    val context = mock(Context::class.java)
    // Set the content with the mocked context
    composeTestRule.setContent { SignifyAppPreview(context, navigationState) }
    composeTestRule.onNodeWithTag("WelcomeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.PROFILE) }
    composeTestRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.AUTH) }
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.FRIENDS) }
    composeTestRule.onNodeWithTag("FriendsListScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("OnSearchButton").performClick()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.EXERCISE_EASY) }
    composeTestRule.onNodeWithTag("ExerciseScreenEasy").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Success").performClick()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.EXERCISE_HARD) }
    composeTestRule.onNodeWithTag("ExerciseScreenHard").assertIsDisplayed()
    composeTestRule.onNodeWithTag("Success").performClick()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.SETTINGS) }
    composeTestRule.onNodeWithTag("SettingsScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.HOME) }
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.HOME) }
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.STATS) }
    composeTestRule.onNodeWithTag("StatsScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.QUEST) }
    composeTestRule.onNodeWithTag("QuestScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.CHALLENGE_HISTORY) }
    composeTestRule.onNodeWithTag("ChallengeHistoryScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Route.NEW_CHALLENGE) }
    composeTestRule.onNodeWithTag("NewChallengeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.CHALLENGE) }
    composeTestRule.onNodeWithTag("ChallengeScreenContent").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.PRACTICE) }
  }
}
