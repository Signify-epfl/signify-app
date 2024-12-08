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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.StatisticsColumnRow

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
      testTagColumn = "MyStatsScreen",
  ) {

    // Top information
    AccountInformation(
        userId = userSession.getUserId()!!,
        userName = userName.value,
        profilePictureUrl = updatedProfilePicture,
        days = streak.value)
    Spacer(modifier = Modifier.height(32.dp))

    // Letters learned
    LearnedLetterList(lettersLearned = lettersLearned.value)
    Spacer(modifier = Modifier.height(64.dp))

    // Number of exercises achieved
    val exercisesText = stringResource(R.string.exercises_text)
    val easyExercisesText = stringResource(R.string.easy_exercises_text)
    val mediumExercisesText = stringResource(R.string.medium_exercises_text)
    val hardExercisesText = stringResource(R.string.hard_exercises_text)
    StatisticsColumnRow(
        columnTestTag = "ExercisesColumn",
        rowTestTag = "ExercisesRow",
        lineText = exercisesText,
        lineTextTag = "ExercisesText",
        statsTextList = listOf(easyExercisesText, mediumExercisesText, hardExercisesText),
        statsNumberList = listOf("${easy.value}", "${medium.value}", "${hard.value}"))
    Spacer(modifier = Modifier.height(32.dp))

    // Number of quests achieved
    val questsText = stringResource(R.string.quests_text)
    val dailyQuestsText = stringResource(R.string.daily_quests_text)
    val weeklyQuestsText = stringResource(R.string.weekly_quests_text)
    StatisticsColumnRow(
        columnTestTag = "QuestsColumn",
        rowTestTag = "QuestsRow",
        lineText = questsText,
        lineTextTag = "QuestsText",
        statsTextList = listOf(dailyQuestsText, weeklyQuestsText),
        statsNumberList = listOf("${daily.value}", "${weekly.value}"))
    Spacer(modifier = Modifier.height(64.dp))
    val graphsAndStatsText = stringResource(R.string.graphs_and_stats_text)
    // Graphs and Stats
    NotImplementedYet(testTag = "GraphsAndStats", text = graphsAndStatsText)
  }
}
