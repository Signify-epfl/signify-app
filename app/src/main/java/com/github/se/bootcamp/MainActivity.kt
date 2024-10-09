package com.github.se.bootcamp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.bootcamp.ui.navigation.BottomNavigationMenu
import com.github.se.bootcamp.ui.navigation.NavigationActions
import com.github.se.bootcamp.ui.navigation.Route
import com.github.se.bootcamp.ui.navigation.TopLevelDestination
import com.github.se.bootcamp.ui.screens.*
import com.github.se.bootcamp.ui.theme.BootcampTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      BootcampTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          SignifyApp()
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
        selectedItem = currentRoute
      )
    }
  ) {
    NavHost(navController = navController, startDestination = Route.MAIN_AIM) {
      composable(Route.MAIN_AIM) { MainAimScreen() }
      composable(Route.PRACTICE) { PracticeScreen() }
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
      composable(Route.QUEST) { QuestScreen() }
      composable(Route.CHALLENGE) { ChallengeScreen() }
    }
  }
}
