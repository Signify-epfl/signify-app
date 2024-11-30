package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
        textColor = MaterialTheme.colorScheme.onPrimary)

    Spacer(modifier = Modifier.height(32.dp)) // Increased space between buttons

    // Create a challenge button
    UtilTextButton(
        onClickAction = { navigationActions.navigateTo(Screen.CREATE_CHALLENGE) },
        testTag = "CreateChallengeButton",
        text = "Create a Challenge",
        backgroundColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onPrimary)

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
                            .height(250.dp) // Set a maximum height to make the box
                            // scrollable
                            .testTag("OngoingChallengesListBox")) {
                      LazyColumn(
                          verticalArrangement = Arrangement.spacedBy(16.dp),
                          modifier = Modifier.testTag("OngoingChallengesLazyColumn")) {
                            items(ongoingChallenges.size) { index ->
                              val challenge = ongoingChallenges[index]
                              OngoingChallengeCard(
                                  challenge = challenge,
                                  onDeleteClick = {
                                    // Delete challenge from user's ongoing list and
                                    // Firestore
                                    userViewModel.removeOngoingChallenge(
                                        userSession.getUserId()!!, challenge.challengeId)
                                    userViewModel.removeOngoingChallenge(
                                        challenge.player2, challenge.challengeId)
                                    challengeViewModel.deleteChallenge(challenge.challengeId)
                                  },
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
    challenge: Challenge, // Using the existing Challenge data class
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
  Card(
      modifier =
          modifier
              .fillMaxWidth()
              .padding(
                  horizontal = 8.dp, vertical = 4.dp) // Padding for better separation between cards
              .border(
                  1.dp,
                  MaterialTheme.colorScheme.onPrimary,
                  RoundedCornerShape(16.dp)), // Adding border for better visual separation
      shape = RoundedCornerShape(16.dp), // Rounded corners
  ) {
    Row(
        modifier =
            Modifier.fillMaxWidth().padding(16.dp), // Padding inside the card for the content
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween // Space between opponent info and delete button
        ) {
          Column(
              verticalArrangement = Arrangement.Center,
              modifier = Modifier.weight(1f) // Take up all available space
              ) {
                // Opponent information
                Text(
                    text = "Opponent: ${challenge.player2}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                Text(
                    text = "Mode: ${challenge.mode}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimary)
              }
          IconButton(
              onClick = onDeleteClick,
              modifier =
                  Modifier.padding(start = 8.dp).testTag("DeleteButton${challenge.challengeId}")) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Challenge",
                    tint = MaterialTheme.colorScheme.error)
              }
        }
  }
}
