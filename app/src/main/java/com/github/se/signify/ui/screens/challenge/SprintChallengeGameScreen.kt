package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CameraBox
import kotlinx.coroutines.delay

@Composable
fun SprintChallengeGameScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    challengeRepository: ChallengeRepository,
    handLandMarkViewModel: HandLandmarkViewModel,
    challengeId: String
) {
  val context = LocalContext.current
  val currentUserId = userSession.getUserId() ?: return

  // State variables
  var currentChallenge by remember { mutableStateOf<Challenge?>(null) }
  var currentWord by remember { mutableStateOf("") }
  var currentLetterIndex by remember { mutableIntStateOf(0) }
  var completedWordCount by remember { mutableIntStateOf(0) }
  var timeLeft by remember { mutableIntStateOf(60) }
  var isGameActive by remember { mutableStateOf(false) }

  // Fetch challenge
  LaunchedEffect(challengeId) {
    challengeRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { challenge ->
          currentChallenge = challenge
          if (challenge.roundWords.isNotEmpty()) {
            currentWord = challenge.roundWords.random()
            isGameActive = true
          } else {
            Toast.makeText(context, "No words available for this sprint", Toast.LENGTH_SHORT).show()
          }
        },
        onFailure = {
          Toast.makeText(context, "Failed to load challenge", Toast.LENGTH_SHORT).show()
        })
  }

  // Timer logic
  LaunchedEffect(isGameActive) {
    if (isGameActive) {
      while (timeLeft > 0) {
        delay(1000L)
        timeLeft--
      }
      isGameActive = false
      updateSprintChallenge(
          context = context,
          currentUserId = currentUserId,
          challengeRepository = challengeRepository,
          challenge = currentChallenge,
          wordsCompleted = completedWordCount,
          navigationActions = navigationActions)
    }
  }

  // Gesture recognition updates
  val landmarksState by handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  LaunchedEffect(landmarksState) {
    if (!landmarksState.isNullOrEmpty() && isGameActive) {
      val currentLetter = currentWord.getOrNull(currentLetterIndex)?.uppercaseChar()
      if (currentLetter != null &&
          detectedGesture.equals(currentLetter.toString(), ignoreCase = true)) {
        currentLetterIndex++
        if (currentLetterIndex == currentWord.length) {
          completedWordCount++
          currentWord = currentChallenge?.roundWords?.random() ?: ""
          currentLetterIndex = 0
        }
      }
    }
  }

  // UI rendering
  if (currentChallenge == null) {
    DisplayLoadingTextSprint()
    return
  }

  AnnexScreenScaffold(navigationActions = navigationActions, testTag = "SprintChallengeScreen") {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
          SprintTimer(timeLeft = timeLeft)

          Spacer(modifier = Modifier.height(16.dp))

          SprintWordDisplay(currentWord = currentWord, currentLetterIndex = currentLetterIndex)

          Spacer(modifier = Modifier.height(16.dp))

          CurrentGestureDisplay(currentWord, currentLetterIndex)

          Spacer(modifier = Modifier.height(16.dp))

          Text(
              text = "Words Completed: $completedWordCount",
              fontSize = 20.sp,
              modifier = Modifier.testTag("CompletedWordsCounterSprint"))

          Spacer(modifier = Modifier.height(32.dp))

          CameraBox(handLandmarkViewModel = handLandMarkViewModel, testTag = "CameraBoxSprint")
        }
  }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CurrentGestureDisplay(currentWord: String, currentLetterIndex: Int) {
  val context = LocalContext.current
  val currentLetter = currentWord.getOrNull(currentLetterIndex)?.uppercaseChar() ?: 'A'
  val imageName = "letter_${currentLetter.lowercase()}"
  val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

  if (imageResId != 0) {
    Box(
        modifier =
            Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(150.dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
                .testTag("CurrentGestureBox"),
        contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = imageResId),
              contentDescription = "Gesture Image",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(120.dp).testTag("CurrentGestureIcon"))
        }
  }
}

@Composable
fun SprintTimer(timeLeft: Int) {
  Text(
      text = "Time Left: $timeLeft seconds",
      fontSize = 24.sp,
      modifier = Modifier.testTag("TimerTextSprint"))
}

@Composable
fun SprintWordDisplay(currentWord: String, currentLetterIndex: Int) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 16.dp)
              .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
              .testTag("WordDisplayBoxSprint")) {
        Text(
            text = buildForegroundTextSprint(currentWord, currentLetterIndex),
            style = TextStyle(fontSize = 36.sp),
            modifier = Modifier.align(Alignment.Center).testTag("CurrentWordTextSprint"))
      }
}

@Composable
fun buildForegroundTextSprint(currentWord: String, currentLetterIndex: Int): AnnotatedString {
  val clampedIndex = currentLetterIndex.coerceAtMost(currentWord.length - 1)
  return buildAnnotatedString {
    append(currentWord.substring(0, clampedIndex))
    if (clampedIndex < currentWord.length) {
      withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
        append(currentWord[clampedIndex].uppercase())
      }
    }
    if (clampedIndex + 1 < currentWord.length) {
      append(currentWord.substring(clampedIndex + 1))
    }
  }
}

@Composable
fun DisplayLoadingTextSprint() {
  Text("Loading challenge...", modifier = Modifier.testTag("LoadingChallengeTextSprint"))
}

fun updateSprintChallenge(
    context: Context,
    currentUserId: String,
    challengeRepository: ChallengeRepository,
    challenge: Challenge?,
    wordsCompleted: Int,
    navigationActions: NavigationActions
) {
  challenge?.let {
    val updatedChallenge =
        it.copy(
            gameStatus = if (it.round == 6) "completed" else "in_progress",
            round = it.round + 1,
            player1RoundCompleted =
                it.player1RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player1) {
                    val nextIndex = this.indexOfFirst { roundCompleted -> !roundCompleted }
                    if (nextIndex != -1)
                        this[nextIndex] = true // Mark the next incomplete round as complete
                  }
                },
            player2RoundCompleted =
                it.player2RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player2) {
                    val nextIndex = this.indexOfFirst { roundCompleted -> !roundCompleted }
                    if (nextIndex != -1)
                        this[nextIndex] = true // Mark the next incomplete round as complete
                  }
                },
            player1WordsCompleted =
                if (currentUserId == it.player1) {
                  it.player1WordsCompleted.toMutableList().apply { add(wordsCompleted) }
                } else it.player1WordsCompleted,
            player2WordsCompleted =
                if (currentUserId == it.player2) {
                  it.player2WordsCompleted.toMutableList().apply { add(wordsCompleted) }
                } else it.player2WordsCompleted)

    challengeRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = { navigationActions.navigateTo(Screen.CHALLENGE) },
        onFailure = {})
  }
}
