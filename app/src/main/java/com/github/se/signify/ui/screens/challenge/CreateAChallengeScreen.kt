package com.github.se.signify.ui.screens.challenge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.UtilButton
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.currentUserId

@Composable
fun CreateAChallengeScreen(
    navigationActions: NavigationActions,
    userRepository: UserRepository,
    challengeRepository: ChallengeRepository,
) {
  val userViewModel: UserViewModel = viewModel(factory = UserViewModel.factory(userRepository))
  val challengeViewModel: ChallengeViewModel =
      viewModel(factory = ChallengeViewModel.factory(challengeRepository))

  // Fetch friends list when this screen is first displayed
  LaunchedEffect(Unit) { userViewModel.getFriendsList(currentUserId) }

  val friends by userViewModel.friends.collectAsState()
  var selectedFriendId by remember { mutableStateOf<String?>(null) }
  var showDialog by remember { mutableStateOf(false) }

    AnnexScreenScaffold(
        navigationActions = navigationActions,
        testTagColumn = "CreateAChallengeContent",
    ) {
        // Title
        Text(
            text = "Select a Friend to Challenge",
            fontSize = 24.sp,
            modifier = Modifier.testTag("ChallengeTitle"))

        Spacer(modifier = Modifier.height(16.dp))

        // Conditional display for friends list
        if (friends.isEmpty()) {
            Text(
                text = "No friends available",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.testTag("NoFriendsText"))
        } else {
            // List of Friends
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f).testTag("FriendsList"),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(friends.size) { index ->
                    val friendId = friends[index]
                    FriendCard(friendId = friendId) {
                        Button(
                            onClick = {
                                selectedFriendId = friendId
                                showDialog = true
                            },
                            modifier =
                            Modifier.padding(8.dp).testTag("ChallengeButton_$friendId")) {
                            Text("Challenge")
                        }
                    }
                }
            }
        }

        // Show Challenge Dialog if showDialog is true
        if (showDialog && selectedFriendId != null) {
            ChallengeModeAlertDialog(
                friendId = selectedFriendId!!,
                onDismiss = { showDialog = false },
                userViewModel = userViewModel,
                challengeViewModel = challengeViewModel)
        }
    }
}

@Composable
fun FriendCard(friendId: String, content: @Composable () -> Unit) {
  Card(
      modifier =
          Modifier.fillMaxWidth()
              .padding(8.dp)
              .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
              .testTag("FriendCard_$friendId"), // Add test tag for each friend card
      shape = RoundedCornerShape(16.dp),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
          // Friend Name
          Text(
              text = friendId,
              fontSize = 20.sp,
              color = Color.Black,
              modifier = Modifier.testTag("FriendName_$friendId"))
          // Content (Challenge Button)
          content()
        }
  }
}

@Composable
fun ChallengeModeAlertDialog(
    friendId: String,
    onDismiss: () -> Unit,
    userViewModel: UserViewModel,
    challengeViewModel: ChallengeViewModel
) {
  var selectedMode by remember { mutableStateOf<String?>(null) }
  val challengeId = remember { "${selectedMode ?: "challenge"}${System.currentTimeMillis()}" }

  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
          UtilTextButton(
              onClickAction = {
                  if (selectedMode != null) {
                      // Create the challenge in the challenges collection
                      challengeViewModel.sendChallengeRequest(
                          currentUserId, friendId, selectedMode!!, challengeId)

                      // Add the challenge to the users' ongoing challenges
                      userViewModel.addOngoingChallenge(currentUserId, challengeId)
                      userViewModel.addOngoingChallenge(friendId, challengeId)

                      onDismiss() // Close the dialog after creating the challenge
                  }
              },
              testTag = "SendChallengeButton",
              text = "Send Challenge",
              backgroundColor = MaterialTheme.colorScheme.primary,
              enabled = selectedMode != null,
          )
      },
      dismissButton = {
          UtilTextButton(
              onClickAction = onDismiss,
              testTag = "CancelButton",
              text = "Cancel",
              backgroundColor = MaterialTheme.colorScheme.surface,
          )
      },
      title = { Text(text = "Pick a Mode", modifier = Modifier.testTag("DialogTitle")) },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // Mode Selection Buttons
          Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
              UtilTextButton(
                    onClickAction = { selectedMode = "sprint" },
                    testTag = "SprintModeButton",
                    text = "Sprint",
                    backgroundColor = if (selectedMode == "sprint") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
              )
                UtilTextButton(
                        onClickAction = { selectedMode = "chrono" },
                        testTag = "ChronoModeButton",
                        text = "Chrono",
                        backgroundColor = if (selectedMode == "chrono") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                )
          }
        }
      })
}
