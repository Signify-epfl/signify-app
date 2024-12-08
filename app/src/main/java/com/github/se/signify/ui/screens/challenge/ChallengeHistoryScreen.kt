package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.stats.StatsRepository
import com.github.se.signify.model.stats.StatsViewModel
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.NotImplementedYet
import com.github.se.signify.ui.common.StatisticsTable

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
    val challengeCategories =
        listOf(
            stringResource(R.string.completed_challenge_text),
            stringResource(R.string.created_challenge_text),
            stringResource(R.string.won_challenge_text))
    // Challenge's statistics
    StatisticsTable(
        lineText = stringResource(id = R.string.number_challenges_text),
        statsTexts = challengeCategories,
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
    NotImplementedYet(
        testTag = "GraphsAndStats", text = stringResource(R.string.graphs_and_stats_text))
  }
}
