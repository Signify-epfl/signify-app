package com.github.se.signify

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.se.signify.model.di.AppDependencyProvider
import com.github.se.signify.model.di.DependencyProvider
import com.github.se.signify.model.exercise.ExerciseLevel
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Route
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.screens.WelcomeScreen
import com.github.se.signify.ui.screens.auth.LoginScreen
import com.github.se.signify.ui.screens.auth.UnauthenticatedScreen
import com.github.se.signify.ui.screens.challenge.ChallengeHistoryScreen
import com.github.se.signify.ui.screens.challenge.ChallengeScreen
import com.github.se.signify.ui.screens.challenge.ChronoChallengeGameScreen
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
import com.github.se.signify.ui.screens.tutorial.TutorialScreen
import com.github.se.signify.ui.theme.SignifyTheme
import com.github.se.signify.ui.updateLanguage

class MainActivity : ComponentActivity() {

  private lateinit var sharedPreferencesTheme: SharedPreferences
  private lateinit var sharedPreferencesLanguage: SharedPreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Load string resources for preference name and key
    val prefName = getString(R.string.pref_name)
    val prefKeyIsDarkTheme = getString(R.string.pref_key_is_dark_theme)
    val prefKeyTutorialCompleted = getString(R.string.pref_key_tutorial_completed)
    val prefKeyTutorialVersion = getString(R.string.pref_key_tutorial_version)
    val prefNameLanguage = getString(R.string.pref_name_language)
    val prefKeyLanguage = getString(R.string.pref_key_is_french)

    // Initialize SharedPreferences
    sharedPreferencesTheme = getSharedPreferences(prefName, Context.MODE_PRIVATE)
    sharedPreferencesLanguage = getSharedPreferences(prefNameLanguage, Context.MODE_PRIVATE)

    // Load the saved theme state (default is false for light mode)
    val savedTheme = sharedPreferences.getBoolean(prefKeyIsDarkTheme, false)
    val savedLanguage = sharedPreferencesLanguage.getBoolean(prefKeyLanguage, false)

    // Tutorial versioning logic
    val currentTutorialVersion = 1 // Update this when the tutorial changes
    val savedTutorialVersion = sharedPreferences.getInt(prefKeyTutorialVersion, 0)

    val isTutorialCompleted =
        sharedPreferences.getBoolean(prefKeyTutorialCompleted, false) &&
            savedTutorialVersion == currentTutorialVersion

    // Update tutorial version if outdated
    if (savedTutorialVersion != currentTutorialVersion) {
      sharedPreferences.edit().putInt(prefKeyTutorialVersion, currentTutorialVersion).apply()
    }

    setContent {
      var isDarkTheme by remember { mutableStateOf(savedTheme) }
      var tutorialCompleted by remember { mutableStateOf(isTutorialCompleted) }
      var isFrenchLanguage by remember { mutableStateOf(savedLanguage) }


      SignifyTheme(darkTheme = isDarkTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
          SignifyAppPreview(
              context = this,
              dependencyProvider = AppDependencyProvider,
              isDarkTheme = isDarkTheme,
              onThemeChange = { isDark ->
                isDarkTheme = isDark
                // Save theme preference
                sharedPreferences.edit().putBoolean(prefKeyIsDarkTheme, isDark).apply()
              },
              tutorialCompleted = tutorialCompleted,
              onTutorialComplete = {
                tutorialCompleted = true
                sharedPreferences.edit().putBoolean(prefKeyTutorialCompleted, true).apply()
              },
              isFrenchLanguage = isFrenchLanguage,
              onLanguageChange = { isFrench ->
                isFrenchLanguage = isFrench
                sharedPreferencesLanguage.edit().putBoolean(prefNameLanguage, isFrench).apply()
                updateLanguage(this, if (isFrench) "fr" else "en")
              })
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
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    tutorialCompleted: Boolean,
    onTutorialComplete: () -> Unit
    isFrenchLanguage: Boolean,
    onLanguageChange: (Boolean) -> Unit
) {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController, dependencyProvider.userSession())
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
      composable(Screen.AUTH.route) {
        LoginScreen(
            navigationActions,
            showTutorial = {
              // Navigate to Tutorial or Home depending on completion
              if (tutorialCompleted) {
                navigationActions.navigateTo(Screen.HOME)
              } else {
                navigationActions.navigateTo(Screen.TUTORIAL)
              }
            })
      }
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

      composable(
          route = Screen.CHRONO_CHALLENGE.route, // Define the base route with `{challengeId}`
          arguments = listOf(navArgument("challengeId") { type = NavType.StringType })) {
              backStackEntry ->
            val challengeId =
                backStackEntry.arguments?.getString("challengeId") ?: return@composable

            // Pass all dependencies
            ChronoChallengeGameScreen(
                navigationActions = navigationActions,
                userSession = dependencyProvider.userSession(),
                challengeRepository = dependencyProvider.challengeRepository(),
                handLandMarkViewModel = handLandMarkViewModel,
                challengeId = challengeId)
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
        startDestination = Screen.TUTORIAL.route,
        route = Route.TUTORIAL,
    ) {
      composable(Screen.TUTORIAL.route) {
        TutorialScreen(navigationActions = navigationActions, onFinish = onTutorialComplete)
      }
    }

    navigation(
        startDestination = Screen.HOME.route,
        route = Route.HOME,
    ) {
      composable(Screen.HOME.route) { HomeScreen(navigationActions) }
      composable(Screen.PRACTICE.route) { ASLRecognition(handLandMarkViewModel, navigationActions) }
      ExerciseLevel.entries.forEach { exerciseLevel ->
        composable(exerciseLevel.screen.route) {
          ExerciseScreen(
              navigationActions,
              handLandMarkViewModel,
              dependencyProvider.userSession(),
              dependencyProvider.statsRepository(),
              exerciseLevel)
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
            dependencyProvider.userRepository(),
            isDarkTheme = isDarkTheme,
            isFrench = isFrenchLanguage,
            onLanguageChange = onLanguageChange,
            onThemeChange = onThemeChange)
      }
    }
  }
}
