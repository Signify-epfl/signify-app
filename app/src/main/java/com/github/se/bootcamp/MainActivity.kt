package com.github.se.bootcamp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.se.bootcamp.model.hand.HandLandMarkImplementation
import com.github.se.bootcamp.model.hand.HandLandMarkViewModel
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.LIST_TOP_LEVEL_DESTINATION
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Route
import com.github.se.bootcamp.ui.navigation.Screen
import com.github.se.bootcamp.ui.navigation.TopLevelDestination
import com.github.se.bootcamp.ui.screens.*
import com.github.se.bootcamp.ui.theme.BootcampTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BootcampTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          val context = LocalContext.current
          val handLandMarkImplementation = HandLandMarkImplementation("hand_landmarker.task","RFC_model_ir9_opset19.onnx")
          val handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
          MainAimScreen(handLandMarkViewModel)
        }
      }
    }
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignifyApp() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController)
  val currentRoute = navController.currentDestination?.route ?: Route.MAIN_AIM

  androidx.compose.material3.Scaffold(
    bottomBar = {
      BottomNavigationMenu(
        onTabSelect = { destination -> navigationActions.navigateTo(destination) },
        tabList = LIST_TOP_LEVEL_DESTINATION,
        selectedItem = currentRoute
      )
    }
  ) {
    NavHost(navController = navController, startDestination = Route.MAIN_AIM) {
      composable(Route.MAIN_AIM) {
        val context = LocalContext.current
        val handLandMarkImplementation = HandLandMarkImplementation("hand_landmarker.task","RFC_model_ir9_opset19.onnx")
        val handLandMarkViewModel = HandLandMarkViewModel(handLandMarkImplementation, context)
        MainAimScreen(handLandMarkViewModel)
      }
      //composable(Route.PRACTICE) { PracticeScreen() }
      composable(Route.PROFILE) { ProfileScreen(
        userId = "Test ID 1",
        userName = "Test Name 1",
        profilePictureUrl = null, // Replace with actual URL or null
        numberOfDays = 30,
        lettersLearned = listOf('A', 'B', 'C', 'D', 'E', 'F'),
        easyExercises = 1,
        hardExercises = 0,
        dailyQuests = 1,
        weeklyQuests = 0,
        onSettingsClick = { /* Handle settings click */ },
        onHelpClick = { /* Handle help click */ },
        onFriendsClick = { /* Handle friends click */ },
        onGraphClick = { /* Handle graph click */ }
      ) }
      //composable(Route.QUEST) { QuestScreen() }
      //composable(Route.CHALLENGE) { ChallengeScreen() }
    }
  }
}

@Composable
fun SignifyAppPreview() {
  val navController = rememberNavController()
  val navigationActions = NavigationActions(navController)

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

    navigation(
      startDestination = Screen.PRACTICE,
      route = Route.PRACTICE,
    ) {
      composable(Screen.PRACTICE) { PracticeScreen(navigationActions) }
    }

    navigation(
      startDestination = Screen.QUEST,
      route = Route.QUEST,
    ) {
      composable(Screen.QUEST) { QuestScreen(navigationActions) }
    }

    navigation(
      startDestination = Screen.PROFILE,
      route = Route.PROFILE,
    ) {
      composable(Screen.PROFILE) { /* Call the profile screen composable with the naviAct as a parameter */ }
    }

    navigation(
      startDestination = Screen.MAIN_AIM,
      route = Route.MAIN_AIM,
    ) {
      composable(Screen.MAIN_AIM) { /* Call the Main screen composable with the naviAct as a parameter */ }
    }
  }
}

