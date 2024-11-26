package com.github.se.signify.ui.screens

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.github.se.signify.SignifyAppPreview
import com.github.se.signify.model.auth.MockUserSession
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.profile.FriendsListScreen
import com.github.se.signify.ui.screens.profile.MyStatsScreen
import com.github.se.signify.ui.screens.profile.ProfileScreen
import com.github.se.signify.ui.screens.profile.SettingsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class MainActivityTest {
  @get:Rule val composeTestRule = createComposeRule()
  private val navigationState = MutableStateFlow<NavigationActions?>(null)
  private val navigationActions = mock(NavigationActions::class.java)
  private val userSession = MockUserSession()
  @get:Rule
  val cameraAccess: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA)

  @Before
  fun setUp() {
    val userRepository = mock(UserRepository::class.java)
    val statsRepository = mock(StatsRepository::class.java)
    val context = mock(Context::class.java)

    composeTestRule.setContent {
      FriendsListScreen(navigationActions, userSession, userRepository)
      SettingsScreen(navigationActions, userSession, userRepository)
      ProfileScreen(navigationActions, userSession, userRepository, statsRepository)
      MyStatsScreen(navigationActions, userSession, userRepository, statsRepository)

      // Set the content with the mocked context
      SignifyAppPreview(context, AppDependencyProvider)
    }
  }

  @Test
  fun testNavigationFromMainActivityAndEveryScreenIsDisplayed() {

    composeTestRule.onNodeWithTag("WelcomeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationActions.navigateTo(Screen.PROFILE) }
    composeTestRule.onNodeWithTag("ProfileScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.AUTH) }
    composeTestRule.onNodeWithTag("LoginScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationActions.navigateTo(Screen.FRIENDS) }
    composeTestRule.onNodeWithTag("FriendsListScreen").assertIsDisplayed()
    composeTestRule.onNodeWithContentDescription("Search").performClick()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.EXERCISE_EASY) }
    composeTestRule.onNodeWithTag("ExerciseScreenEasy").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.EXERCISE_MEDIUM) }
    composeTestRule.onNodeWithTag("ExerciseScreenMedium").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.EXERCISE_HARD) }
    composeTestRule.onNodeWithTag("ExerciseScreenHard").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationActions.navigateTo(Screen.SETTINGS) }
    composeTestRule.onNodeWithTag("SettingsScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.HOME) }
    composeTestRule.onNodeWithTag("HomeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationActions.navigateTo(Screen.STATS) }
    composeTestRule.onNodeWithTag("MyStatsScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.CHALLENGE_HISTORY) }
    composeTestRule.onNodeWithTag("ChallengeHistoryScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.CHALLENGE) }
    composeTestRule.onNodeWithTag("ChallengeScreen").assertIsDisplayed()

    composeTestRule.runOnIdle { navigationState.value?.navigateTo(Screen.PRACTICE) }
  }
}
