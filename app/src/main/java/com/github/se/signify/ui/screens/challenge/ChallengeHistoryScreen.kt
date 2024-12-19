package com.github.se.signify.ui.screens.challenge

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.StatisticsTable

@Composable
fun ChallengeHistoryScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    challengeRepository: ChallengeRepository
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val challengeViewModel: ChallengeViewModel =
      viewModel(factory = ChallengeViewModel.factory(userSession, challengeRepository))

  val challengesCompleted = remember { mutableIntStateOf(0) }
  val challengesCreated = remember { mutableIntStateOf(0) }
  val challengesWon = remember { mutableIntStateOf(0) }

  val pastChallengeIds by userViewModel.pastChallenges.collectAsState()
  LaunchedEffect(pastChallengeIds) { challengeViewModel.getChallenges(pastChallengeIds) }
  val pastChallenges by challengeViewModel.challenges.collectAsState()

  LaunchedEffect(Unit) {
    userViewModel.getFriendsList()
    userViewModel.getPastChallenges()

    // TODO: Move this to `UserViewModel`
    userRepository.getChallengesCompleted(
        userId = userSession.getUserId()!!,
        onSuccess = { completed -> challengesCompleted.intValue = completed },
        onFailure = { exception ->
          Log.e("ChallengeStats", "Failed to fetch challenges completed: ${exception.message}")
          challengesCompleted.intValue = 0 // Default value in case of failure
        })

    userRepository.getChallengesCreated(
        userId = userSession.getUserId()!!,
        onSuccess = { created -> challengesCreated.intValue = created },
        onFailure = { exception ->
          Log.e("ChallengeStats", "Failed to fetch challenges created: ${exception.message}")
          challengesCreated.intValue = 0 // Default value in case of failure
        })

    userRepository.getChallengesWon(
        userId = userSession.getUserId()!!,
        onSuccess = { won -> challengesWon.intValue = won },
        onFailure = { exception ->
          Log.e("ChallengeStats", "Failed to fetch challenges won: ${exception.message}")
          challengesWon.intValue = 0 // Default value in case of failure
        })
  }

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "ChallengeHistoryScreen",
  ) {
    val challengeCategories =
        listOf(
            stringResource(R.string.completed_challenge_text),
            stringResource(R.string.created_challenge_text),
            stringResource(R.string.won_challenge_text))

    Spacer(modifier = Modifier.height(32.dp))
    StatisticsTable(
        lineText = stringResource(id = R.string.number_challenges_text),
        statsTexts = challengeCategories,
        statsNumberList =
            listOf(
                "${challengesCompleted.intValue}",
                "${challengesCreated.intValue}",
                "${challengesWon.intValue}"),
        columnTestTag = "ChallengesColumn",
        rowTestTag = "ChallengesRow",
        lineTextTestTag = "ChallengesText")

    Spacer(modifier = Modifier.height(32.dp))

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .testTag("PastChallengesBox")) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.past_challenges_title),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("PastChallengesTitle"))

            Spacer(modifier = Modifier.height(24.dp))

            if (pastChallenges.isEmpty()) {
              Text(
                  text = stringResource(R.string.no_past_challenges),
                  fontSize = 16.sp,
                  color = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.testTag("NoPastChallengesText"))
            } else {
              Box(
                  modifier =
                      Modifier.fillMaxWidth().height(300.dp).testTag("PastChallengesListBox")) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.testTag("PastChallengesList")) {
                          items(pastChallenges.size) { index ->
                            val challenge = pastChallenges[index]
                            PastChallengeCard(challenge, userSession)
                          }
                        }
                  }
            }
          }
        }
  }
}

@Composable
fun PastChallengeCard(challenge: Challenge, userSession: UserSession) {
  val currentUserId = userSession.getUserId()
  val opponent = if (challenge.player1 == currentUserId) challenge.player2 else challenge.player1
  val mode = challenge.mode
  val player1Result = calculatePlayerResult(challenge, isPlayer1 = true)
  val player2Result = calculatePlayerResult(challenge, isPlayer1 = false)

  val winner =
      determineWinner(
          challenge.mode, challenge.player1, challenge.player2, player1Result, player2Result)

  Log.d(
      "PastChallengeCard",
      "Rendering card with Opponent=$opponent, Mode=$mode, Player1Score=$player1Result, Player2Score=$player2Result, Winner=$winner")

  Card(
      modifier =
          Modifier.fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
      shape = RoundedCornerShape(16.dp),
  ) {
    Column(Modifier.padding(16.dp)) {
      Text(text = "Opponent: $opponent", fontSize = 16.sp)
      Text(text = "Mode: $mode", fontSize = 16.sp)
      PlayerScoreText(player = challenge.player1, result = player1Result, mode = mode)
      PlayerScoreText(player = challenge.player2, result = player2Result, mode = mode)
      Text(text = "Winner: $winner", fontSize = 16.sp)
    }
  }
}

fun calculatePlayerResult(challenge: Challenge, isPlayer1: Boolean): Double {
  return when (challenge.mode) {
    ChallengeMode.SPRINT.toString() -> {
      if (isPlayer1) challenge.player1WordsCompleted.sum().toDouble()
      else challenge.player2WordsCompleted.sum().toDouble()
    }
    else -> {
      if (isPlayer1) challenge.player1Times.sum() / 1000.0
      else challenge.player2Times.sum() / 1000.0
    }
  }
}

fun determineWinner(
    mode: String,
    player1: String,
    player2: String,
    player1Result: Double,
    player2Result: Double
): String {
  return when (mode) {
    ChallengeMode.SPRINT.toString() -> {
      when {
        player1Result > player2Result -> player1
        player2Result > player1Result -> player2
        else -> "Draw"
      }
    }
    else -> {
      when {
        player1Result < player2Result -> player1
        player2Result < player1Result -> player2
        else -> "Draw"
      }
    }
  }
}

@Composable
fun PlayerScoreText(player: String, result: Double, mode: String) {
  val scoreText =
      when (mode) {
        ChallengeMode.CHRONO.toString() -> "$player Score: $result s"
        else -> "$player Score: $result words"
      }
  Text(text = scoreText, fontSize = 16.sp)
}
