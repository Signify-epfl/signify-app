package com.github.se.signify.ui.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsViewModel
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.ui.common.AccountInformation
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.LearnedLetterList
import com.github.se.signify.ui.common.NotImplementedYet
import com.github.se.signify.ui.common.StatisticsTable

@Composable
fun MyStatsScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    statsRepository: StatsRepository,
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val statsViewModel: StatsViewModel =
      viewModel(factory = StatsViewModel.factory(userSession, statsRepository))

  LaunchedEffect(Unit) {
    userViewModel.getUserName()
    userViewModel.getProfilePictureUrl()
    userViewModel.updateStreak()
    userViewModel.getStreak()
    statsViewModel.getEasyExerciseStats()
    statsViewModel.getMediumExerciseStats()
    statsViewModel.getHardExerciseStats()
    statsViewModel.getDailyQuestStats()
    statsViewModel.getWeeklyQuestStats()
  }

  val userName = userViewModel.userName.collectAsState()
  val streak = userViewModel.streak.collectAsState()
  val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()
  val lettersLearned = statsViewModel.lettersLearned.collectAsState()
  val easy = statsViewModel.easy.collectAsState()
  val medium = statsViewModel.medium.collectAsState()
  val hard = statsViewModel.hard.collectAsState()
  val daily = statsViewModel.daily.collectAsState()
  val weekly = statsViewModel.weekly.collectAsState()
  var updatedProfilePicture by remember { mutableStateOf(profilePictureUrl.value) }

  LaunchedEffect(profilePictureUrl.value) { updatedProfilePicture = profilePictureUrl.value }

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "MyStatsScreen",
  ) {

    // Top information
    AccountInformation(
        userId = userSession.getUserId()!!,
        userName = userName.value,
        profilePictureUrl = updatedProfilePicture,
        streak = streak.value)
    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    LearnedLetterList(lettersLearned = lettersLearned.value)
    Spacer(modifier = Modifier.height(64.dp))

    // Number of exercises achieved
    StatisticsTable(
        lineText = "Number of exercises achieved :",
        statsTexts = listOf("Easy", "Medium", "Hard"),
        statsNumberList = listOf("${easy.value}", "${medium.value}", "${hard.value}"),
        columnTestTag = "ExercisesColumn",
        rowTestTag = "ExercisesRow",
        lineTextTestTag = "ExercisesText")
    Spacer(modifier = Modifier.height(32.dp))

    // Number of quests achieved
    StatisticsTable(
        lineText = "Number of quests achieved :",
        statsTexts = listOf("Daily", "Weekly"),
        statsNumberList = listOf("${daily.value}", "${weekly.value}"),
        columnTestTag = "QuestsColumn",
        rowTestTag = "QuestsRow",
        lineTextTestTag = "QuestsText")
    Spacer(modifier = Modifier.height(64.dp))

    // Graphs and Stats
    NotImplementedYet(text = "Graphs and Stats", testTag = "GraphsAndStats")
  }
}
