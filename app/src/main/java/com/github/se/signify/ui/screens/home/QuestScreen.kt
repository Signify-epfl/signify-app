package com.github.se.signify.ui.screens.home

import android.util.Log
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.VisibleForTesting
import androidx.camera.lifecycle.ProcessCameraProvider
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.common.user.UserRepository
import com.github.se.signify.model.common.user.UserViewModel
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.home.quest.Quest
import com.github.se.signify.model.home.quest.QuestRepository
import com.github.se.signify.model.home.quest.QuestViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CameraBox

@Composable
fun QuestScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    questRepository: QuestRepository,
    userRepository: UserRepository,
    handLandMarkViewModel: HandLandmarkViewModel
) {
  val questViewModel: QuestViewModel = viewModel(factory = QuestViewModel.factory(questRepository))
  val userViewModel: UserViewModel =
      viewModel(factory = UserViewModel.factory(userSession, userRepository))

  val quests = questViewModel.quest.collectAsState()
  LaunchedEffect(userSession.getUserId()) {
    userViewModel.checkAndUnlockNextQuest()
    userViewModel.fetchCompletedQuests()
  }

  val unlockedQuests by userViewModel.unlockedQuests.collectAsState()

  AnnexScreenScaffold(
      navigationActions = navigationActions,
      testTag = "QuestScreen",
  ) {
    LazyColumn(
        modifier = Modifier.weight(1f),
    ) {
      item { Spacer(modifier = Modifier.height(32.dp)) }
      item { QuestTitle() }
      items(quests.value.size) { index ->
        val isUnlocked = index < unlockedQuests.toInt()
        QuestBox(
            quest = quests.value[index],
            isUnlocked,
            handLandMarkViewModel = handLandMarkViewModel,
            userViewModel = userViewModel)
      }
    }
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun QuestBox(
    quest: Quest,
    isUnlocked: Boolean,
    handLandMarkViewModel: HandLandmarkViewModel,
    userViewModel: UserViewModel
) {
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
                Text(
                    if (isUnlocked) stringResource(R.string.open_quest_button_text)
                    else stringResource(R.string.closed_quest_button_text))
              }
        }
      }
  // Display the dialog if the state is true
  if (isDialogVisible) {
    QuestDescriptionDialog(
        quest = quest,
        onDismiss = { isDialogVisible = false },
        handLandMarkViewModel = handLandMarkViewModel,
        userViewModel = userViewModel,
    )
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun QuestDescriptionDialog(
    quest: Quest,
    onDismiss: () -> Unit,
    handLandMarkViewModel: HandLandmarkViewModel,
    userViewModel: UserViewModel,
) {
  val inSignLanguageText = stringResource(R.string.in_sign_language_text)
  val completedQuests by userViewModel.completedQuests.collectAsState(emptyList())
  val isCompleted = completedQuests.contains(quest.index)

  var isFingerspellVisible by remember { mutableStateOf(false) }

  if (isFingerspellVisible) {
    FingerSpellDialog(
        word = quest.title,
        onDismiss = {
          isFingerspellVisible = false
          onDismiss()
        },
        handLandMarkViewModel = handLandMarkViewModel,
        userViewModel = userViewModel,
        questIndex = quest.index)
  } else {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.background,
        title = {
          Text(
              text = "${quest.title} $inSignLanguageText",
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
                    setVideoPath(quest.videoPath)

                    // Prepare the video and start playback
                    setOnPreparedListener { mediaPlayer ->
                      mediaPlayer.isLooping = true // Loop video automatically
                      start()
                    }
                  }
                },
                modifier =
                    Modifier.fillMaxWidth().height(200.dp).align(Alignment.CenterHorizontally))

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = quest.description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary)
          }
        },
        confirmButton = {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                if (isCompleted) {
                  Text(
                      text = "Completed",
                      fontSize = 16.sp,
                      color = MaterialTheme.colorScheme.primary,
                      modifier = Modifier.padding(16.dp))
                } else {
                  Button(
                      onClick = { isFingerspellVisible = true },
                      colors =
                          ButtonDefaults.buttonColors(
                              containerColor = MaterialTheme.colorScheme.secondary,
                              contentColor = MaterialTheme.colorScheme.onSecondary)) {
                        Text(stringResource(R.string.try_fingerspell_button_text))
                      }
                }

                Button(
                    onClick = onDismiss,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary)) {
                      Text(stringResource(R.string.close_text))
                    }
              }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(16.dp))
  }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun QuestTitle() {
  Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
      modifier =
          Modifier.fillMaxWidth()
              .background(MaterialTheme.colorScheme.background)
              .padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = stringResource(R.string.quest_screen_title_text),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            color = MaterialTheme.colorScheme.primary)
      }
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun FingerSpellDialog(
    word: String,
    onDismiss: () -> Unit,
    handLandMarkViewModel: HandLandmarkViewModel,
    userViewModel: UserViewModel,
    questIndex: String
) {
  val context = LocalContext.current
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }
  val landmarksState = handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()
  val toastMessage = stringResource(R.string.word_completed_text)

  if (!landmarksState.value.isNullOrEmpty()) {
    // Gesture matching logic
    handleGestureMatchingForWord(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        word = word,
        onProgressUpdate = { newIndex -> currentLetterIndex = newIndex },
        onWordComplete = {
          // Mark the quest as completed
          userViewModel.markQuestAsCompleted(questIndex)
          Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
          onDismiss() // Close dialog when word is completed
        })
  }
  // Ensure Camera is stopped when the dialog is dismissed
  DisposableEffect(Unit) {
    onDispose {
      val cameraProvider = ProcessCameraProvider.getInstance(context).get()
      cameraProvider.unbindAll() // Unbind the camera
    }
  }

  AlertDialog(
      onDismissRequest = { onDismiss() },
      containerColor = MaterialTheme.colorScheme.background,
      title = {
        Text(
            text = stringResource(R.string.fingerspell_title_text, word),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary)
      },
      text = {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
          Text(
              text = stringResource(R.string.try_fingerspell_text, word),
              fontSize = 16.sp,
              color = MaterialTheme.colorScheme.onBackground,
              modifier = Modifier.padding(bottom = 16.dp))

          WordLayer(word = word, currentLetterIndex = currentLetterIndex)

          // Camera view to detect gestures
          CameraBox(handLandMarkViewModel)
        }
      },
      confirmButton = {
        Button(
            onClick = onDismiss,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)) {
              Text(stringResource(R.string.close_text))
            }
      })
}

@Composable
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun WordLayer(
    word: String,
    currentLetterIndex: Int,
) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 16.dp)
              .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .border(2.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .testTag("sentenceLayer"),
      contentAlignment = Alignment.Center) {
        Text(
            text = buildForegroundText(word, currentLetterIndex),
            modifier = Modifier.testTag("CurrentWordTag"),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSecondary)
      }
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun handleGestureMatchingForWord(
    detectedGesture: String,
    currentLetterIndex: Int,
    word: String,
    onProgressUpdate: (newLetterIndex: Int) -> Unit,
    onWordComplete: () -> Unit
) {
  if (currentLetterIndex >= word.length) {
    return
  }

  val currentLetter = word[currentLetterIndex].toString().uppercase()

  if (detectedGesture == currentLetter) {
    if (currentLetterIndex == word.length - 1) {
      onWordComplete()
    } else {
      onProgressUpdate(currentLetterIndex + 1)
    }
  } else {
    Log.d(
        "GestureMatching",
        "Detected gesture ($detectedGesture) does not match the current letter ($currentLetter)")
  }
}
