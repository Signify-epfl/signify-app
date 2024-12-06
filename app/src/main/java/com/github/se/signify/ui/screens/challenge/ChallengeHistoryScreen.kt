package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.NotImplementedYet
import com.github.se.signify.ui.StatisticsTable

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChallengeHistoryScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    statsRepository: StatsRepository,
) {
  val statsViewModel: StatsViewModel =
      viewModel(factory = StatsViewModel.factory(userSession, statsRepository))

  val challengesCompleted = statsViewModel.completed.collectAsState()
  val challengesCreated = statsViewModel.created.collectAsState()
  val challengesWon = statsViewModel.won.collectAsState()

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "ChallengeHistoryScreen",
  ) {
    // Challenge's statistics
    StatisticsTable(
        lineText = "Number of challenges :",
        statsTexts = listOf("Completed", "Created", "Won"),
        statsNumberList =
            listOf(
                "${challengesCompleted.value}",
                "${challengesCreated.value}",
                "${challengesWon.value}"),
        columnTestTag = "ChallengesColumn",
        rowTestTag = "ChallengesRow",
        lineTextTestTag = "ChallengesText")
    Spacer(modifier = Modifier.height(32.dp))

    // Graphs and Stats
    NotImplementedYet(text = "Graphs and Stats", testTag = "GraphsAndStats")
  }
}
