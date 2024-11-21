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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.di.DependencyProvider
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
import com.github.se.signify.ui.screens.home.ExerciseScreenMedium
import com.github.se.signify.ui.screens.home.FeedbackScreen
import com.github.se.signify.ui.screens.home.HomeScreen
import com.github.se.signify.ui.screens.home.QuestScreen
import com.github.se.signify.ui.screens.home.QuizScreen
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
          SignifyAppPreview(context, AppDependencyProvider, navigationState)
        }
      }
    }
  }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SignifyAppPreview(
    context: Context,
    dependencyProvider: DependencyProvider,
    navigationState: MutableStateFlow<NavigationActions?>
) {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController)
  val handLandMarkImplementation = dependencyProvider.handLandMarkRepository()
  val handLandMarkViewModel: HandLandMarkViewModel =
      viewModel(factory = HandLandMarkViewModel.provideFactory(context, handLandMarkImplementation))
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
      composable(Screen.AUTH) { LoginScreen(navigationActions, dependencyProvider.userSession()) }
    }

    navigation(
        startDestination = Screen.CHALLENGE,
        route = Route.CHALLENGE,
    ) {
      composable(Screen.CHALLENGE) { ChallengeScreen(navigationActions) }
      composable(Screen.NEW_CHALLENGE) {
        NewChallengeScreen(
            navigationActions,
            dependencyProvider.userRepository(),
            dependencyProvider.challengeRepository())
      }
      composable(Screen.CREATE_CHALLENGE) {
        CreateAChallengeScreen(
            navigationActions,
            dependencyProvider.userRepository(),
            dependencyProvider.challengeRepository())
      }
      composable(Screen.CHALLENGE_HISTORY) {
        ChallengeHistoryScreen(navigationActions, dependencyProvider.statsRepository())
      }
    }

    navigation(
        startDestination = Screen.HOME,
        route = Route.HOME,
    ) {
      composable(Screen.HOME) { HomeScreen(navigationActions) }
      composable(Screen.PRACTICE) { ASLRecognition(handLandMarkViewModel, navigationActions) }
      composable(Screen.EXERCISE_EASY) {
        ExerciseScreenEasy(navigationActions, handLandMarkViewModel)
      }
      composable(Screen.EXERCISE_MEDIUM) {
        ExerciseScreenMedium(navigationActions, handLandMarkViewModel)
      }

      composable(Screen.EXERCISE_HARD) {
        ExerciseScreenHard(navigationActions, handLandMarkViewModel)
      }
      composable(Screen.FEEDBACK) { FeedbackScreen(navigationActions) }
      composable(Screen.QUEST) {
        QuestScreen(
            navigationActions,
            dependencyProvider.questRepository(),
            dependencyProvider.userRepository())
      }
      composable(Screen.QUIZ) {
        QuizScreen(
            navigationActions,
            dependencyProvider.quizRepository(),
        )
      }
    }

    navigation(
        startDestination = Screen.PROFILE,
        route = Route.PROFILE,
    ) {
      composable(Screen.PROFILE) {
        ProfileScreen(
            navigationActions,
            dependencyProvider.userRepository(),
            dependencyProvider.statsRepository())
      }
      composable(Screen.FRIENDS) {
        FriendsListScreen(navigationActions, dependencyProvider.userRepository())
      }
      composable(Screen.STATS) {
        MyStatsScreen(
            navigationActions,
            dependencyProvider.userRepository(),
            dependencyProvider.statsRepository())
      }
      composable(Screen.SETTINGS) {
        SettingsScreen(navigationActions, dependencyProvider.userRepository())
      }
    }
  }
  navigationState.value = navigationActions
}
