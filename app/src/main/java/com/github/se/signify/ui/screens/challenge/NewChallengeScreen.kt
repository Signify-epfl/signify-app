package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.TextButton

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewChallengeScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    challengeRepository: ChallengeRepository,
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val challengeViewModel: ChallengeViewModel =
      viewModel(factory = ChallengeViewModel.factory(userSession, challengeRepository))

  LaunchedEffect(Unit) {
    userViewModel.getFriendsList()
    userViewModel.getOngoingChallenges()
  }

  val ongoingChallenges by userViewModel.ongoingChallenges.collectAsState()

  AnnexScreenScaffold(navigationActions = navigationActions, testTag = "NewChallengeScreen") {
    // My Friends button
    TextButton(
        onClick = { navigationActions.navigateTo(Screen.FRIENDS) },
        testTag = "MyFriendsButton",
        text = "My Friends",
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier)

    Spacer(modifier = Modifier.height(32.dp))

    // Create a challenge button
    TextButton(
        onClick = { navigationActions.navigateTo(Screen.CREATE_CHALLENGE) },
        testTag = "CreateChallengeButton",
        text = "Create a Challenge",
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier)

    Spacer(modifier = Modifier.height(32.dp))

    // Ongoing Challenges Section
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .border(2.dp, MaterialTheme.colorScheme.onPrimary)
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .testTag("OngoingChallengesBox")) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.testTag("OngoingChallengesContent")) {
                Text(
                    text = "My Ongoing Challenges",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("OngoingChallengesTitle"))

                Spacer(modifier = Modifier.height(24.dp))

                // Scrollable Ongoing Challenges List
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .height(250.dp)
                            .testTag("OngoingChallengesListBox")) {
                      LazyColumn(
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          modifier = Modifier.testTag("OngoingChallengesLazyColumn")) {
                            items(ongoingChallenges.size) { index ->
                              val challenge = ongoingChallenges[index]

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
                                    Log.d(
                                        "Navigation",
                                        "Navigating with challengeId: ${challenge.challengeId}")
                                    navigationActions.navigateTo(
                                        Screen.CHRONO_CHALLENGE,
                                        params = mapOf("challengeId" to challenge.challengeId))
                                  },
                                  userSession = userSession,
                                  modifier = Modifier.testTag("OngoingChallengeCard$index"))
                            }
                          }
                    }
              }
        }
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

  // Determine if the current player has completed all rounds
  val currentUserId = userSession.getUserId()
  val isChallengeCompleted =
      if (currentUserId == challenge.player1) {
        challenge.player1RoundCompleted.all { it }
      } else {
        challenge.player2RoundCompleted.all { it }
      }

  // Calculate the personal total time if the challenge is completed
  val totalTime =
      if (isChallengeCompleted) {
        if (currentUserId == challenge.player1) {
          challenge.player1Times.sum() / 1000
        } else {
          challenge.player2Times.sum() / 1000
        }
      } else null

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
            Text(
                text = "Opponent: $opponentName",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(
                text = "Mode: ${challenge.mode}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface)
            if (isChallengeCompleted) {
              Text(
                  text = "Your Total Time: ${totalTime}s",
                  fontSize = 14.sp,
                  color = MaterialTheme.colorScheme.onSurface)
            }
          }

          // Play Button
          Box(
              modifier =
                  Modifier.padding(start = 8.dp)
                      .size(48.dp) // Set size to ensure consistency between buttons
              ) {
                IconButton(
                    onClick = {
                      if (isChallengeCompleted) {
                        Toast.makeText(
                                context,
                                "Challenge already completed, wait for result",
                                Toast.LENGTH_SHORT)
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
                    onClick = onDeleteClick,
                    modifier =
                        Modifier.fillMaxSize() // Make the button fill the Box size
                            .testTag("DeleteButton${challenge.challengeId}")) {
                      Icon(
                          imageVector = Icons.Default.Delete,
                          contentDescription = "Delete Challenge",
                          tint = Color.Gray, // Explicitly set color for visibility
                          modifier = Modifier.size(30.dp) // Icon size for better visibility
                          )
                    }
              }
        }
  }
}
