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
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsViewModel
import com.github.se.signify.ui.common.AccountInformation
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CreateGraph
import com.github.se.signify.ui.common.LearnedLetterList
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
      statsViewModel.getTimePerLetter()
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
  val timePerLetter = statsViewModel.timePerLetter.collectAsState()
  var updatedProfilePicture by remember { mutableStateOf(profilePictureUrl.value) }

  LaunchedEffect(profilePictureUrl.value) { updatedProfilePicture = profilePictureUrl.value }

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "MyStatsScreen",
  ) {
    Spacer(modifier = Modifier.height(32.dp))

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
    val exercisesText = stringResource(R.string.completed_exercise_count_text)
    val easyExercisesText = stringResource(R.string.easy_exercises_text)
    val mediumExercisesText = stringResource(R.string.medium_exercises_text)
    val hardExercisesText = stringResource(R.string.hard_exercises_text)
    StatisticsTable(
        columnTestTag = "ExercisesColumn",
        rowTestTag = "ExercisesRow",
        lineText = exercisesText,
        statsTexts = listOf(easyExercisesText, mediumExercisesText, hardExercisesText),
        statsNumberList = listOf("${easy.value}", "${medium.value}", "${hard.value}"),
        lineTextTestTag = "ExercisesText")
    Spacer(modifier = Modifier.height(32.dp))

    // Number of quests achieved
    val questsText = stringResource(R.string.completed_quest_count_text)
    val dailyQuestsText = stringResource(R.string.daily_quests_text)
    val weeklyQuestsText = stringResource(R.string.weekly_quests_text)
    StatisticsTable(
        columnTestTag = "QuestsColumn",
        rowTestTag = "QuestsRow",
        lineText = questsText,
        statsTexts = listOf(dailyQuestsText, weeklyQuestsText),
        statsNumberList = listOf("${daily.value}", "${weekly.value}"),
        lineTextTestTag = "QuestsText")
    Spacer(modifier = Modifier.height(64.dp))
    // Graphs and Stats
    CreateGraph(timePerLetter = timePerLetter.value)
  }
}
