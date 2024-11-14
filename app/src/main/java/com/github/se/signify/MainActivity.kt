package com.github.se.signify

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.se.signify.model.hand.HandLandMarkConfig
import com.github.se.signify.model.hand.HandLandMarkImplementation
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.*
import com.github.se.signify.ui.screens.challenge.ChallengeHistoryScreen
import com.github.se.signify.ui.screens.challenge.ChallengeScreen
import com.github.se.signify.ui.screens.challenge.CreateAChallengeScreen
import com.github.se.signify.ui.screens.challenge.NewChallengeScreen
import com.github.se.signify.ui.screens.home.ASLRecognition
import com.github.se.signify.ui.screens.home.ExerciseScreenEasy
import com.github.se.signify.ui.screens.home.ExerciseScreenHard
import com.github.se.signify.ui.screens.home.HomeScreen
import com.github.se.signify.ui.screens.home.QuestScreen
import com.github.se.signify.ui.screens.profile.FriendsListScreen
import com.github.se.signify.ui.screens.profile.MyStatsScreen
import com.github.se.signify.ui.screens.profile.ProfileScreen
import com.github.se.signify.ui.screens.profile.SettingsScreen
import com.github.se.signify.ui.theme.SignifyTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SignifyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val context = LocalContext.current
          val navigationState = MutableStateFlow<NavigationActions?>(null)
          SignifyAppPreview(context, navigationState)
        }
      }
    }
  }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SignifyAppPreview(context: Context, navigationState: MutableStateFlow<NavigationActions?>) {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController)
  val handLandMarkImplementation =
      HandLandMarkImplementation(HandLandMarkConfig("hand_landmarker.task", "RFC_model_ir9_opset19.onnx"))
  val handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
  NavHost(navController = navController, startDestination = Route.WELCOME) {
    navigation(
        startDestination = Screen.WELCOME,
        route = Route.WELCOME,
    ) {
      composable(Screen.WELCOME) { WelcomeScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.AUTH,
        route = Route.AUTH,
    ) {
      composable(Screen.AUTH) { LoginScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.CHALLENGE,
        route = Route.CHALLENGE,
    ) {
      composable(Screen.CHALLENGE) { ChallengeScreen(navigationActions) }
    }

    composable(Route.NEW_CHALLENGE) { NewChallengeScreen(navigationActions) }
    composable(Route.CREATE_CHALLENGE) { CreateAChallengeScreen(navigationActions) }
    composable(Route.CHALLENGE_HISTORY) { ChallengeHistoryScreen(navigationActions, 1, 1) }
    navigation(
        startDestination = Screen.QUEST,
        route = Route.QUEST,
    ) {
      composable(Screen.QUEST) { QuestScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.HOME,
        route = Route.HOME,
    ) {
      composable(Screen.HOME) { HomeScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.PROFILE,
        route = Route.PROFILE,
    ) {
      composable(Screen.PROFILE) { ProfileScreen(navigationActions = navigationActions) }

      composable(Route.FRIENDS) { FriendsListScreen(navigationActions) }

      composable(Route.STATS) {
        MyStatsScreen(
            navigationActions = navigationActions,
            userId = "Test ID 1",
            userName = "Test Name 1",
            profilePictureUrl = null, // Replace with actual URL or null
            numberOfDays = 30,
            lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
            exercisesAchieved = listOf(10, 3),
            questsAchieved = listOf(3, 0))
      }

      composable(Route.SETTINGS) { SettingsScreen(navigationActions) }
    }
    composable(Screen.PRACTICE) { ASLRecognition(handLandMarkViewModel, navigationActions) }
    composable(Screen.EXERCISE_EASY) {
      ExerciseScreenEasy(navigationActions, handLandMarkViewModel)
    }
    composable(Screen.EXERCISE_HARD) {
      ExerciseScreenHard(navigationActions, handLandMarkViewModel)
    }
  }
  navigationState.value = navigationActions
}
