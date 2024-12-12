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
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.StatisticsTable

@Composable
fun ChallengeHistoryScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository
) {
    val userViewModel: UserViewModel =
        viewModel(factory = UserViewModel.factory(userSession, userRepository))

    val challengesCompleted = remember { mutableIntStateOf(0) }
    val challengesCreated = remember { mutableIntStateOf(0) }
    val challengesWon = remember { mutableIntStateOf(0) }
    val pastChallenges by userViewModel.pastChallenges.collectAsState()

    LaunchedEffect(Unit) {
        challengesCompleted.intValue = userRepository.getChallengesCompleted(userSession.getUserId()!!)
        challengesCreated.intValue = userRepository.getChallengesCreated(userSession.getUserId()!!)
        challengesWon.intValue = userRepository.getChallengesWon(userSession.getUserId()!!)
        userViewModel.getPastChallenges()
    }

    AnnexScreenScaffold(
        navigationActions = navigationActions,
        testTag = "ChallengeHistoryScreen",
    ) {
        val challengeCategories =
            listOf(
                stringResource(R.string.completed_challenge_text),
                stringResource(R.string.created_challenge_text),
                stringResource(R.string.won_challenge_text)
            )

        Spacer(modifier = Modifier.height(32.dp))
        StatisticsTable(
            lineText = stringResource(id = R.string.number_challenges_text),
            statsTexts = challengeCategories,
            statsNumberList = listOf(
                "${challengesCompleted.intValue}",
                "${challengesCreated.intValue}",
                "${challengesWon.intValue}"
            ),
            columnTestTag = "ChallengesColumn",
            rowTestTag = "ChallengesRow",
            lineTextTestTag = "ChallengesText"
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .testTag("PastChallengesBox")
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.past_challenges_title),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("PastChallengesTitle")
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (pastChallenges.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_past_challenges),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.testTag("NoPastChallengesText")
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .testTag("PastChallengesListBox")
                    ) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.testTag("PastChallengesList")
                        ) {
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
    val player1Score = challenge.player1Times?.sum()?.div(1000) ?: 0
    val player2Score = challenge.player2Times?.sum()?.div(1000) ?: 0
    val opponent = if (challenge.player1 == currentUserId) challenge.player2 else challenge.player1
    val mode = challenge.mode
    val winner = challenge.winner ?: "No Winner"

    Log.d(
        "PastChallengeCard",
        "Rendering card with Opponent=$opponent, Mode=$mode, Player1Score=$player1Score, Player2Score=$player2Score, Winner=$winner"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Opponent: $opponent", fontSize = 16.sp)
            Text(text = "Mode: $mode", fontSize = 16.sp)
            Text(text = "${challenge.player1} Score: $player1Score s", fontSize = 16.sp)
            Text(text = "${challenge.player2} Score: $player2Score s", fontSize = 16.sp)
            Text(text = "Winner: $winner", fontSize = 16.sp)
        }
    }
}
