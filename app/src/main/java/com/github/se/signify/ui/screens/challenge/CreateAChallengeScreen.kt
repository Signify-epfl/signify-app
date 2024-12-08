package com.github.se.signify.ui.screens.challenge

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.UtilTextButton

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
    val challengeTitleText = stringResource(R.string.challenge_title_text)
    Text(text = challengeTitleText, fontSize = 24.sp, modifier = Modifier.testTag("ChallengeTitle"))

    Spacer(modifier = Modifier.height(16.dp))

    // Conditional display for friends list
    if (friends.isEmpty()) {
      Text(
          text = stringResource(R.string.no_friend_text),
          fontSize = 18.sp,
          color = Color.Gray,
          modifier = Modifier.testTag("NoFriendsText"))
    } else {
      // List of Friends
      val challengeText = stringResource(R.string.challenge_text)
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
                    text = challengeText,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary)
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
            text = stringResource(R.string.send_challenge_text),
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = selectedMode.value != null,
        )
      },
      dismissButton = {
        UtilTextButton(
            onClickAction = onDismiss,
            testTag = "CancelButton",
            text = stringResource(R.string.cancel_text),
            backgroundColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface)
      },
      title = {
        Text(
            text = stringResource(R.string.pick_a_mode_string),
            modifier = Modifier.fillMaxWidth().testTag("DialogTitle"))
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
      textColor =
          if (selectedMode.value == mode) MaterialTheme.colorScheme.onPrimary
          else MaterialTheme.colorScheme.onSurface)
}
