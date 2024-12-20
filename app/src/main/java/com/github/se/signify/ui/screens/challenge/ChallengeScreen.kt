package com.github.se.signify.ui.screens.challenge

import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestinations
import com.github.se.signify.ui.common.BasicButton
import com.github.se.signify.ui.common.FriendsButton
import com.github.se.signify.ui.common.HelpText
import com.github.se.signify.ui.common.MainScreenScaffold
import com.github.se.signify.ui.common.TextButton

private const val msToSecondsDivision = 1000

@Composable
fun ChallengeScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    challengeRepository: ChallengeRepository,
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val challengeViewModel: ChallengeViewModel =
      viewModel(factory = ChallengeViewModel.factory(userSession, challengeRepository))

  val ongoingChallengeIds by userViewModel.ongoingChallengeIds.collectAsState()
  LaunchedEffect(ongoingChallengeIds) { challengeViewModel.getChallenges(ongoingChallengeIds) }
  val ongoingChallenges by challengeViewModel.challenges.collectAsState()

  LaunchedEffect(Unit) {
    userViewModel.getFriendsList()
    userViewModel.getOngoingChallenges()
  }
  val done = remember { mutableStateOf(false) }

  MainScreenScaffold(
      navigationActions = navigationActions,
      topLevelDestination = TopLevelDestinations.CHALLENGE,
      testTag = "ChallengeScreen",
      helpText =
          HelpText(
              title = stringResource(R.string.challenge_screen_title),
              content = stringResource(R.string.description_challenge_text)),
      topBarButtons =
          listOf(
              { ChallengeHistoryButton(navigationActions) }, { FriendsButton(navigationActions) }),
  ) {
    Spacer(modifier = Modifier.height(32.dp))

    // Create a challenge button
    val createAChallengeText = stringResource(R.string.create_a_challenge_text)
    TextButton(
        onClick = { navigationActions.navigateTo(Screen.CREATE_CHALLENGE) },
        testTag = "CreateChallengeButton",
        text = createAChallengeText,
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary)

    Spacer(modifier = Modifier.height(32.dp))

    // Ongoing Challenges Section
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .testTag("OngoingChallengesBox")) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.testTag("OngoingChallengesContent")) {
                val myOnGoingChallengeText = stringResource(R.string.my_ongoing_challenges_text)
                Text(
                    text = myOnGoingChallengeText,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("OngoingChallengesTitle"))

                Spacer(modifier = Modifier.height(24.dp))

                // Scrollable Ongoing Challenges List
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .heightIn(min = 400.dp, max = 800.dp)
                            .testTag("OngoingChallengesListBox")) {
                      LazyColumn(
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          modifier = Modifier.testTag("OngoingChallengesLazyColumn")) {
                            items(ongoingChallenges.size) { index ->
                              val challenge = ongoingChallenges[index]
                              val isBothCompleted =
                                  challenge.player1RoundCompleted.all { it } &&
                                      challenge.player2RoundCompleted.all { it }

                              if (isBothCompleted && !done.value) {
                                // Determine the winner
                                done.value = true

                                val player1Result =
                                    calculatePlayerResult(challenge, isPlayer1 = true)
                                val player2Result =
                                    calculatePlayerResult(challenge, isPlayer1 = false)

                                val winner =
                                    determineWinner(
                                        challenge.mode,
                                        challenge.player1,
                                        challenge.player2,
                                        player1Result,
                                        player2Result,
                                        stringResource(R.string.draw))

                                // Update the challenge in pastChallenges
                                synchronized(this) { // Synchronize updates to avoid race conditions
                                  userViewModel.removeOngoingChallenge(
                                      challenge.player1, challenge.challengeId)
                                  userViewModel.addPastChallenge(
                                      challenge.player1, challenge.challengeId)
                                  userViewModel.removeOngoingChallenge(
                                      challenge.player2, challenge.challengeId)
                                  userViewModel.addPastChallenge(
                                      challenge.player2, challenge.challengeId)
                                  userViewModel.incrementField(winner, "challengesWon")
                                  userViewModel.incrementField(
                                      challenge.player2, "challengesCompleted")
                                  userViewModel.incrementField(
                                      challenge.player1, "challengesCompleted")

                                  challengeRepository.updateWinner(
                                      challenge.challengeId, winner, {}, {})
                                }
                              }
                              OngoingChallengeCard(
                                  challenge = challenge,
                                  onDeleteClick = {
                                    userViewModel.removeOngoingChallenge(
                                        userSession.getUserId()!!, challenge.challengeId)
                                    userViewModel.removeOngoingChallenge(
                                        challenge.player2, challenge.challengeId)
                                    challengeViewModel.deleteChallenge(challenge.challengeId)
                                  },
                                  onPlayClick = {
                                    val destination =
                                        when (challenge.mode) {
                                          ChallengeMode.CHRONO.toString() -> Screen.CHRONO_CHALLENGE
                                          else -> Screen.SPRINT_CHALLENGE
                                        }
                                    navigationActions.navigateTo(
                                        destination,
                                        params = mapOf("challengeId" to challenge.challengeId))
                                  },
                                  userSession = userSession,
                                  modifier = Modifier.testTag("OngoingChallengeCard$index"))
                            }
                          }
                    }
              }
        }

    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun OngoingChallengeCard(
    challenge: Challenge,
    onDeleteClick: () -> Unit,
    onPlayClick: () -> Unit,
    userSession: UserSession,
    modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  // State to control dialog visibility
  val showDialog = remember { mutableStateOf(false) }

  // Determine if the current player has completed all rounds
  val currentUserId = userSession.getUserId()
  val isChallengeCompleted =
      if (currentUserId == challenge.player1) {
        challenge.player1RoundCompleted.all { it }
      } else {
        challenge.player2RoundCompleted.all { it }
      }
  val resultTextTime = stringResource(R.string.your_total_time)
  val resultTextWords = stringResource(R.string.your_total_words)
  // Calculate the personal total time if the challenge is completed
  val displayText =
      if (isChallengeCompleted) {
        when (challenge.mode) {
          ChallengeMode.SPRINT.toString() -> {
            val totalWords =
                if (currentUserId == challenge.player1) {
                  challenge.player1WordsCompleted.sum()
                } else {
                  challenge.player2WordsCompleted.sum()
                }
            "$resultTextWords $totalWords"
          }
          ChallengeMode.CHRONO.toString() -> {
            val totalTime =
                if (currentUserId == challenge.player1) {
                  challenge.player1Times.sum() / msToSecondsDivision
                } else {
                  challenge.player2Times.sum() / msToSecondsDivision
                }
            "$resultTextTime ${totalTime}s"
          }
          else -> ""
        }
      } else {
        null
      }

  Card(
      modifier =
          modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
              .background(
                  MaterialTheme.colorScheme
                      .primary), // Explicitly set background color for better contrast
      shape = RoundedCornerShape(16.dp),
  ) {
    var round = findNextIndex(challenge, currentUserId!!)
    if (round == -1) {
      round = 3
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
          // Opponent Info Column
          Column(verticalArrangement = Arrangement.Center, modifier = Modifier.weight(1f)) {
            val opponentName =
                if (currentUserId == challenge.player1) {
                  challenge.player2
                } else {
                  challenge.player1
                }
            val opponentText = stringResource(R.string.opponent_text)
            Text(
                text = "$opponentText: $opponentName",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "${stringResource(R.string.mode_text)}: ${challenge.mode}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "Round: $round/3",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface)
            if (displayText != null) {
              Text(
                  text = displayText, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
            }
          }

          // Play Button
          Box(
              modifier =
                  Modifier.padding(start = 8.dp)
                      .size(48.dp) // Set size to ensure consistency between buttons
              ) {
                val challengeAlreadyCompletedText =
                    stringResource(R.string.challenge_completed_text)
                IconButton(
                    onClick = {
                      if (isChallengeCompleted) {
                        Toast.makeText(context, challengeAlreadyCompletedText, Toast.LENGTH_SHORT)
                            .show()
                      } else {
                        onPlayClick()
                      }
                    },
                    modifier =
                        Modifier.fillMaxSize() // Make the button fill the Box size
                            .testTag("PlayButton${challenge.challengeId}")) {
                      Icon(
                          imageVector = Icons.Default.PlayArrow,
                          contentDescription = "Play Challenge",
                          tint =
                              if (isChallengeCompleted) Color.Gray
                              else MaterialTheme.colorScheme.primary,
                          modifier = Modifier.size(36.dp) // Icon size for better visibility
                          )
                    }
              }

          // Delete Button
          Box(
              modifier =
                  Modifier.padding(start = 8.dp)
                      .size(48.dp) // Set size to ensure consistency between buttons
              ) {
                IconButton(
                    onClick = { showDialog.value = true },
                    modifier =
                        Modifier.fillMaxSize() // Make the button fill the Box size
                            .testTag("DeleteButton${challenge.challengeId}")) {
                      Icon(
                          imageVector = Icons.Default.Delete,
                          contentDescription = "Delete Challenge",
                          tint = MaterialTheme.colorScheme.error,
                          modifier = Modifier.size(30.dp) // Icon size for better visibility
                          )
                    }
              }
        }
  }

  // Confirmation dialog
  DeletionConfirmationDialog(showDialog = showDialog, onClickAction = onDeleteClick)
}

@Composable
fun DeletionConfirmationDialog(showDialog: MutableState<Boolean>, onClickAction: () -> Unit) {
  if (showDialog.value) {
    val context = LocalContext.current

    Dialog(onDismissRequest = { showDialog.value = false }) {
      Surface(
          shape = RoundedCornerShape(16.dp),
          color = MaterialTheme.colorScheme.surface,
          modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("ConfirmationDialog")) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                  val confirmingDeleteChallengeText =
                      stringResource(R.string.confirm_challenge_deletion_text)
                  Text(
                      text = confirmingDeleteChallengeText,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface,
                      modifier = Modifier.padding(bottom = 16.dp))
                  Row(
                      horizontalArrangement = Arrangement.SpaceEvenly,
                      modifier = Modifier.fillMaxWidth()) {
                        val removedFriendText = stringResource(R.string.removed_challenge_text)
                        val yesText = stringResource(R.string.yes_button_text)
                        val noText = stringResource(R.string.no_button_text)
                        DialogButton(
                            onClick = {
                              onClickAction()
                              Toast.makeText(context, removedFriendText, Toast.LENGTH_SHORT).show()
                              showDialog.value = false
                            },
                            contentColor = MaterialTheme.colorScheme.onError,
                            containerColor = MaterialTheme.colorScheme.error,
                            text = yesText)
                        DialogButton(
                            onClick = { showDialog.value = false },
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary,
                            text = noText)
                      }
                }
          }
    }
  }
}

@Composable
fun DialogButton(onClick: () -> Unit, containerColor: Color, contentColor: Color, text: String) {
  Button(
      onClick = { onClick() },
      colors =
          ButtonDefaults.buttonColors(
              containerColor = containerColor, contentColor = contentColor)) {
        Text(text)
      }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun ChallengeHistoryButton(navigationActions: NavigationActions) {
  BasicButton(
      onClick = { navigationActions.navigateTo(Screen.CHALLENGE_HISTORY) },
      iconTestTag = "ChallengeHistoryIcon",
      contentDescription = "Challenge History",
      modifier = Modifier.testTag("ChallengeHistoryButton"),
      painter = painterResource(id = R.drawable.historyicon))
}
