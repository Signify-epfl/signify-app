package com.github.se.signify.ui.screens.home

import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.quest.Quest
import com.github.se.signify.model.quest.QuestRepository
import com.github.se.signify.model.quest.QuestViewModel
import com.github.se.signify.model.user.UserRepository
import com.github.se.signify.model.user.UserViewModel
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun QuestScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    questRepository: QuestRepository,
    userRepository: UserRepository,
) {
  // ViewModels
  val questViewModel: QuestViewModel = viewModel(factory = QuestViewModel.factory(questRepository))
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))

  // Collect quest data
  val quests = questViewModel.quest.collectAsState()
  val unlockedQuests = 2
  // by userViewModel.unlockedQuests.collectAsState()

  Scaffold(
      modifier = Modifier.fillMaxSize().testTag("QuestScreen"),
  ) { padding ->
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
      // Header Row
      Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier =
              Modifier.fillMaxWidth()
                  .background(MaterialTheme.colorScheme.background)
                  .padding(horizontal = 16.dp, vertical = 8.dp)) {
            IconButton(onClick = { navigationActions.goBack() }) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = stringResource(R.string.quest_screen_title),
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary)
          }

      // Quests List
      LazyColumn(
          contentPadding = PaddingValues(vertical = 8.dp),
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(padding)) {
            items(quests.value.size) { index ->
              val isUnlocked = index < unlockedQuests.toInt()
              QuestBox(quest = quests.value[index], isUnlocked = isUnlocked)
            }
          }
    }
  }
}

@Composable
fun QuestBox(quest: Quest, isUnlocked: Boolean) {
  // State to manage dialog visibility
  var isDialogVisible by remember { mutableStateOf(false) }
  Card(
      modifier = Modifier.fillMaxWidth().padding(16.dp).testTag("QuestCard"),
      elevation = CardDefaults.elevatedCardElevation(4.dp),
      colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
          Text(
              text = quest.title,
              color = MaterialTheme.colorScheme.onPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth().testTag("QuestHeader"),
              textAlign = TextAlign.Left)

          Spacer(modifier = Modifier.height(20.dp))
          Button(
              modifier = Modifier.fillMaxWidth().testTag("QuestActionButton"),
              onClick = { if (isUnlocked) isDialogVisible = true },
              colors =
                  ButtonDefaults.buttonColors(
                      containerColor = MaterialTheme.colorScheme.background,
                      contentColor = MaterialTheme.colorScheme.primary),
              shape = RoundedCornerShape(50),
              enabled = isUnlocked) {
                Text(if (isUnlocked) "Let’s Go!" else "Locked")
              }
        }
      }
  // Display the dialog if the state is true
  if (isDialogVisible) {
    QuestDescriptionDialog(quest = quest, onDismiss = { isDialogVisible = false })
  }
}

@Composable
fun QuestDescriptionDialog(quest: Quest, onDismiss: () -> Unit) {
  AlertDialog(
      onDismissRequest = { onDismiss() },
      containerColor = MaterialTheme.colorScheme.background,
      title = {
        Text(
            text = quest.title + " in sign language",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary)
      },
      text = {
        Column(modifier = Modifier.wrapContentSize().padding(8.dp)) {
          // Display the video using VideoView
          AndroidView(
              factory = { context ->
                VideoView(context).apply {
                  setVideoPath(quest.videoPath) // Set video path (local or remote)

                  // Prepare the video and start playback
                  setOnPreparedListener { mediaPlayer ->
                    mediaPlayer.isLooping = true // Loop video automatically
                    start()
                  }
                }
              },
              modifier = Modifier.fillMaxWidth().height(200.dp).align(Alignment.CenterHorizontally))

          Spacer(modifier = Modifier.height(20.dp))

          Text(
              text = quest.description, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        }
      },
      confirmButton = {
        Button(
            onClick = onDismiss,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
              Text("Close")
            }
      },
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier.padding(16.dp))
}
