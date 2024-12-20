package com.github.se.signify.ui.screens.challenge

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.ChallengeMode
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.challenge.ChallengeViewModel
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.TextButton

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
      testTag = "CreateAChallengeContent",
  ) {
    Spacer(modifier = Modifier.height(32.dp))

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
                TextButton(
                    onClick = {
                      selectedFriendId = friendId
                      showDialog = true
                    },
                    testTag = "ChallengeButton_$friendId",
                    text = challengeText,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
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
              .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
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
  val context = LocalContext.current
  val selectedMode = remember { mutableStateOf<ChallengeMode?>(null) }
  val challengeId = remember { "${selectedMode.value ?: "challenge"}${System.currentTimeMillis()}" }
  val sendChallengeText = stringResource(R.string.send_challenge)

  AlertDialog(
      onDismissRequest = onDismiss,
      confirmButton = {
        TextButton(
            onClick = {
              if (selectedMode.value != null) {
                val words = getWordsForMode(context, selectedMode.value!!)
                challengeViewModel.sendChallengeRequest(
                    friendId = friendId,
                    mode = selectedMode.value!!,
                    challengeId = challengeId,
                    roundWords = words)
                userViewModel.addOngoingChallenge(userSession.getUserId()!!, challengeId)
                userViewModel.addOngoingChallenge(friendId, challengeId)
                userViewModel.incrementField(userSession.getUserId()!!, "challengesCreated")
                userViewModel.incrementField(friendId, "challengesCreated")
                onDismiss()
              }
            },
            testTag = "SendChallengeButton",
            text = sendChallengeText,
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            enabled = selectedMode.value != null,
        )
      },
      dismissButton = {
        TextButton(
            onClick = onDismiss,
            testTag = "CancelButton",
            text = stringResource(R.string.cancel_text),
            backgroundColor = MaterialTheme.colorScheme.onSurface,
            textColor = MaterialTheme.colorScheme.surface,
        )
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
  TextButton(
      onClick = { selectedMode.value = mode },
      testTag = "${mode.modeName}ModeButton",
      text = mode.modeName,
      backgroundColor =
          if (selectedMode.value == mode) MaterialTheme.colorScheme.primary
          else MaterialTheme.colorScheme.surface,
      textColor =
          if (selectedMode.value == mode) MaterialTheme.colorScheme.onPrimary
          else MaterialTheme.colorScheme.onSurface,
  )
}

fun getWordsForMode(context: Context, mode: ChallengeMode): List<String> {
  val allWords = context.resources.getStringArray(R.array.challenge_word_list).toList()
  return when (mode) {
    ChallengeMode.CHRONO -> allWords.shuffled().take(3)
    ChallengeMode.SPRINT -> allWords.shuffled().take(25)
  }
}
