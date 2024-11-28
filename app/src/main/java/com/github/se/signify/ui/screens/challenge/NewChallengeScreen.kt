package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen

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

  // Fetch friends list and ongoing challenges when this screen is first displayed
  LaunchedEffect(Unit) {
    userViewModel.getFriendsList()
    userViewModel.getOngoingChallenges()
  }

  val ongoingChallenges by userViewModel.ongoingChallenges.collectAsState()

  AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = "NewChallengeScreen") {
    // My Friends button
    UtilTextButton(
        onClickAction = { navigationActions.navigateTo(Screen.FRIENDS) },
        testTag = "MyFriendsButton",
        text = "My Friends",
        backgroundColor = MaterialTheme.colorScheme.primary,
    )

    Spacer(modifier = Modifier.height(32.dp)) // Increased space between buttons

    // Create a challenge button
    UtilTextButton(
        onClickAction = { navigationActions.navigateTo(Screen.CREATE_CHALLENGE) },
        testTag = "CreateChallengeButton",
        text = "Create a Challenge",
        backgroundColor = MaterialTheme.colorScheme.primary,
    )

    Spacer(modifier = Modifier.height(32.dp)) // Increased space between buttons and the box

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

                Spacer(
                    modifier =
                        Modifier.height(24.dp)) // Increased space between title and challenges

                // Scrollable Ongoing Challenges List
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .height(250.dp) // Set a maximum height to make the box scrollable
                            .testTag("OngoingChallengesListBox")) {
                      LazyColumn(
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          modifier = Modifier.testTag("OngoingChallengesLazyColumn")) {
                            items(ongoingChallenges.size) { index ->
                              val challenge = ongoingChallenges[index]

                              OngoingChallengeCard(
                                  challenge = challenge,
                                  onDeleteClick = {
                                    // Delete challenge from user's ongoing list and Firestore
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
                                    // Navigate to the ChronoChallengeGameScreen using the
                                    // challengeId
                                    navigationActions.navigateTo(
                                        Screen.chronoChallengeWithId(challenge.challengeId))
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
          challenge.player1Times.sum() / 1000 // Divide by 1000 to get the time in seconds
        } else {
          challenge.player2Times.sum() / 1000 // Divide by 1000 to get the time in seconds
        }
      } else null

  Card(
      modifier =
          modifier
              .fillMaxWidth()
              .padding(horizontal = 8.dp, vertical = 4.dp)
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp)),
      shape = RoundedCornerShape(16.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
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
                color = MaterialTheme.colorScheme.surface,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(
                text = "Mode: ${challenge.mode}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.surface)
            if (isChallengeCompleted) {
              Text(
                  text = "Your Total Time: ${totalTime}s",
                  fontSize = 14.sp,
                  color = MaterialTheme.colorScheme.surface)
            }
          }

          // Play button always displayed
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
              modifier = Modifier.padding(start = 8.dp)) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Challenge",
                    tint =
                        if (isChallengeCompleted) Color.Gray else MaterialTheme.colorScheme.primary)
              }

          // Always show delete button
          IconButton(
              onClick = onDeleteClick,
              modifier =
                  Modifier.padding(start = 8.dp).testTag("DeleteButton${challenge.challengeId}")) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Challenge",
                    tint = MaterialTheme.colorScheme.surface)
              }
        }
  }
}
