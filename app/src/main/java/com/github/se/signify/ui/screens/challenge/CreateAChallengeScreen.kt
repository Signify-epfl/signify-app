package com.github.se.signify.ui.screens.challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.BackButton
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.screens.profile.currentUserId

@Composable
fun CreateAChallengeScreen(
    navigationActions: NavigationActions,
    userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
    challengeViewModel: ChallengeViewModel = viewModel(factory = ChallengeViewModel.Factory)
) {
  // Fetch friends list when this screen is first displayed
  LaunchedEffect(Unit) { userViewModel.getFriendsList(currentUserId) }

  val friends by userViewModel.friends.collectAsState()
  var selectedFriendId by remember { mutableStateOf<String?>(null) }
  var showDialog by remember { mutableStateOf(false) }

  Scaffold(
      topBar = { BackButton { navigationActions.goBack() } },
      content = { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
              // Title
              Text(text = "Select a Friend to Challenge", fontSize = 24.sp)

              Spacer(modifier = Modifier.height(16.dp))

              // List of Friends
              LazyColumn(
                  modifier = Modifier.fillMaxSize(),
                  verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(friends.size) { index ->
                      val friendId = friends[index]
                      FriendCard(friendId = friendId) {
                        Button(
                            onClick = {
                              selectedFriendId = friendId
                              showDialog = true
                            },
                            modifier = Modifier.padding(8.dp)) {
                              Text("Challenge")
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
      })
}

@Composable
fun FriendCard(friendId: String, content: @Composable () -> Unit) {
  Card(
      modifier = Modifier.fillMaxWidth().padding(8.dp),
  ) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = friendId, fontSize = 20.sp)
      Spacer(modifier = Modifier.height(8.dp))
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
        Button(
            onClick = {
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
            enabled = selectedMode != null) {
              Text("Send Challenge")
            }
      },
      dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } },
      title = { Text(text = "Choose Challenge Mode") },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          // Mode Selection Buttons
          Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { selectedMode = "sprint" },
                colors =
                    buttonColors(
                        containerColor = if (selectedMode == "sprint") Color.Blue else Color.Gray),
                modifier = Modifier.padding(8.dp)) {
                  Text("Sprint")
                }

            Button(
                onClick = { selectedMode = "chrono" },
                colors =
                    buttonColors(
                        containerColor = if (selectedMode == "chrono") Color.Blue else Color.Gray),
                modifier = Modifier.padding(8.dp)) {
                  Text("Chrono")
                }
          }
        }
      })
}
