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
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AccountInformation
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.LearnedLetterList
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.StatisticsRow
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun MyStatsScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    statsRepository: StatsRepository,
    userViewModel: UserViewModel =
        viewModel(factory = UserViewModel.factory(userSession, userRepository)),
    statsViewModel: StatsViewModel =
        viewModel(factory = StatsViewModel.factory(userSession, statsRepository))
) {

  LaunchedEffect(Unit) {
    userViewModel.getUserName()
    userViewModel.getProfilePictureUrl()
    userViewModel.updateStreak()
    userViewModel.getStreak()
  }

  val userName = userViewModel.userName.collectAsState()
  val streak = userViewModel.streak.collectAsState()
  val profilePictureUrl = userViewModel.profilePictureUrl.collectAsState()
  val lettersLearned = statsViewModel.lettersLearned.collectAsState()
  val easy = statsViewModel.easy.collectAsState()
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
    StatisticsRow(
        rowTestTag = "ExercisesRow",
        lineText = "Number of exercises achieved",
        lineTextTag = "ExercisesText",
        columnTextList = listOf(listOf("EASY", "${easy.value}"), listOf("HARD", "${hard.value}")),
        columnTextSPList = listOf(listOf(12, 20), listOf(12, 20)),
        columnTextTagList = listOf("ExercisesEasyCountBox", "ExercisesHardCountBox"))
    Spacer(modifier = Modifier.height(12.dp))

    // Number of quests achieved
    StatisticsRow(
        rowTestTag = "QuestsRow",
        lineText = "Number of quests achieved",
        lineTextTag = "QuestsText",
        columnTextList =
            listOf(listOf("DAILY", "${daily.value}"), listOf("WEEKLY", "${weekly.value}")),
        columnTextSPList = listOf(listOf(12, 20), listOf(12, 20)),
        columnTextTagList = listOf("DailyQuestCountBox", "WeeklyQuestsCountBox"))
    Spacer(modifier = Modifier.height(64.dp))

    // Graphs and Stats
    NotImplementedYet(testTag = "GraphsAndStats", text = "Graphs and Stats")
  }
}
