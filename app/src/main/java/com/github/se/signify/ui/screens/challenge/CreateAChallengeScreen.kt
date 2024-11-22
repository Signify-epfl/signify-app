package com.github.se.signify.ui.screens.challenge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.UtilTextButton
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun CreateAChallengeScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    userRepository: UserRepository,
    challengeRepository: ChallengeRepository,
) {
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))
  val challengeViewModel: ChallengeViewModel =
      viewModel(factory = ChallengeViewModel.factory(userSession, challengeRepository))

  // Fetch friends list when this screen is first displayed
  LaunchedEffect(Unit) { userViewModel.getFriendsList() }

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
                UtilTextButton(
                    onClickAction = {
                      selectedFriendId = friendId
                      showDialog = true
                    },
                    testTag = "ChallengeButton_$friendId",
                    text = "Challenge",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                )
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
        userSession = userSession,
        userViewModel = userViewModel,
        challengeViewModel = challengeViewModel)
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

          Spacer(modifier = Modifier.width(16.dp))

          // Content (Challenge Button)
          content()
        }
  }
}

@Composable
fun ChallengeModeAlertDialog(
    friendId: String,
    onDismiss: () -> Unit,
    userSession: UserSession,
    userViewModel: UserViewModel,
    challengeViewModel: ChallengeViewModel
) {
  val selectedMode = remember { mutableStateOf<ChallengeMode?>(null) }
  val challengeId = remember { "${selectedMode.value ?: "challenge"}${System.currentTimeMillis()}" }

  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        UtilTextButton(
            onClickAction = {
              if (selectedMode.value != null) {
                // Create the challenge in the challenges collection
                challengeViewModel.sendChallengeRequest(friendId, selectedMode.value!!, challengeId)

                // Add the challenge to the users' ongoing challenges
                userViewModel.addOngoingChallenge(userSession.getUserId()!!, challengeId)
                userViewModel.addOngoingChallenge(friendId, challengeId)

                onDismiss() // Close the dialog after creating the challenge
              }
            },
            testTag = "SendChallengeButton",
            text = "Send Challenge",
            backgroundColor = MaterialTheme.colorScheme.primary,
            enabled = selectedMode.value != null,
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
      title = {
        Text(text = "Pick a Mode", modifier = Modifier.fillMaxWidth().testTag("DialogTitle"))
      },
      text = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
          ChallengeMode.entries.forEach { mode ->
            Box(Modifier.weight(1f)) {
              ModeButton(
                  mode = mode,
                  selectedMode = selectedMode,
              )
            }
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
  )
}

@Composable
fun ModeButton(
    mode: ChallengeMode,
    selectedMode: MutableState<ChallengeMode?>,
) {
  UtilTextButton(
      onClickAction = { selectedMode.value = mode },
      testTag = "${mode.modeName}ModeButton",
      text = mode.modeName,
      backgroundColor =
          if (selectedMode.value == mode) MaterialTheme.colorScheme.primary
          else MaterialTheme.colorScheme.surface,
  )
}
