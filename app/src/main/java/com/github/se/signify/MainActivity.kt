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
import com.github.se.signify.model.exercise.ExerciseLevel
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Route
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.WelcomeScreen
import com.github.se.signify.ui.screens.auth.LoginScreen
import com.github.se.signify.ui.screens.auth.UnauthenticatedScreen
import com.github.se.signify.ui.screens.challenge.ChallengeHistoryScreen
import com.github.se.signify.ui.screens.challenge.ChallengeScreen
import com.github.se.signify.ui.screens.challenge.CreateAChallengeScreen
import com.github.se.signify.ui.screens.challenge.NewChallengeScreen
import com.github.se.signify.ui.screens.home.ASLRecognition
import com.github.se.signify.ui.screens.home.ExerciseScreen
import com.github.se.signify.ui.screens.home.FeedbackScreen
import com.github.se.signify.ui.screens.home.HomeScreen
import com.github.se.signify.ui.screens.home.QuestScreen
import com.github.se.signify.ui.screens.home.QuizScreen
import com.github.se.signify.ui.screens.profile.FriendsListScreen
import com.github.se.signify.ui.screens.profile.MyStatsScreen
import com.github.se.signify.ui.screens.profile.ProfileScreen
import com.github.se.signify.ui.screens.profile.SettingsScreen
import com.github.se.signify.ui.theme.SignifyTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SignifyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val context = LocalContext.current
          SignifyAppPreview(context, AppDependencyProvider)
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
) {
  val navController = rememberNavController()
  val navigationActions =
      NavigationActions(context, navController, dependencyProvider.userSession())
  val handLandMarkImplementation = dependencyProvider.handLandMarkRepository()
  val handLandMarkViewModel: HandLandMarkViewModel =
      viewModel(factory = HandLandMarkViewModel.provideFactory(context, handLandMarkImplementation))
  NavHost(navController = navController, startDestination = Route.WELCOME) {
    navigation(
        startDestination = Screen.WELCOME.route,
        route = Route.WELCOME,
    ) {
      composable(Screen.WELCOME.route) { WelcomeScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.AUTH.route,
        route = Route.AUTH,
    ) {
      composable(Screen.AUTH.route) { LoginScreen(navigationActions) }
      composable(Screen.UNAUTHENTICATED.route) { UnauthenticatedScreen(navigationActions) }
    }

    navigation(
        startDestination = Screen.CHALLENGE.route,
        route = Route.CHALLENGE,
    ) {
      composable(Screen.CHALLENGE.route) { ChallengeScreen(navigationActions) }
      composable(Screen.NEW_CHALLENGE.route) {
        NewChallengeScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository(),
            dependencyProvider.challengeRepository())
      }
      composable(Screen.CREATE_CHALLENGE.route) {
        CreateAChallengeScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository(),
            dependencyProvider.challengeRepository())
      }
      composable(Screen.CHALLENGE_HISTORY.route) {
        ChallengeHistoryScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.statsRepository())
      }
    }

    navigation(
        startDestination = Screen.HOME.route,
        route = Route.HOME,
    ) {
      composable(Screen.HOME.route) { HomeScreen(navigationActions) }
      composable(Screen.PRACTICE.route) { ASLRecognition(handLandMarkViewModel, navigationActions) }
      ExerciseLevel.entries.forEach { exerciseLevel ->
        composable(exerciseLevel.levelScreen.route) {
          ExerciseScreen(navigationActions, handLandMarkViewModel, exerciseLevel)
        }
      }
      composable(Screen.FEEDBACK.route) {
        FeedbackScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.feedbackRepository())
      }
      composable(Screen.QUEST.route) {
        QuestScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.questRepository(),
            dependencyProvider.userRepository())
      }
      composable(Screen.QUIZ.route) {
        QuizScreen(
            navigationActions,
            dependencyProvider.quizRepository(),
        )
      }
    }

    navigation(
        startDestination = Screen.PROFILE.route,
        route = Route.PROFILE,
    ) {
      composable(Screen.PROFILE.route) {
        ProfileScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository(),
            dependencyProvider.statsRepository())
      }
      composable(Screen.FRIENDS.route) {
        FriendsListScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository())
      }
      composable(Screen.STATS.route) {
        MyStatsScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository(),
            dependencyProvider.statsRepository())
      }
      composable(Screen.SETTINGS.route) {
        SettingsScreen(
            navigationActions,
            dependencyProvider.userSession(),
            dependencyProvider.userRepository())
      }
    }
  }
}
