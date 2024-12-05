package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun ChronoChallengeGameScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    challengeRepository: ChallengeRepository,
    handLandMarkViewModel: HandLandMarkViewModel,
    challengeId: String
) {
  val context = LocalContext.current
  val currentUserId = userSession.getUserId() ?: return

  // Mutable state for tracking challenge information
  var currentChallenge by remember { mutableStateOf<Challenge?>(null) }
  var currentWord by remember { mutableStateOf("") }
  var startTime by remember { mutableLongStateOf(0L) }
  var elapsedTime by remember { mutableLongStateOf(0L) }
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }
  var typedWord by rememberSaveable { mutableStateOf("") }
  var isGameActive by remember { mutableStateOf(false) }

  // Fetch the current challenge
  LaunchedEffect(challengeId) {
    challengeRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { challenge ->
          currentChallenge = challenge
          if (challenge.roundWords.isNotEmpty() &&
              challenge.round > 0 &&
              challenge.round <= challenge.roundWords.size) {
            currentWord = challenge.roundWords[challenge.round - 1]
            currentLetterIndex = 0
            typedWord = ""
            isGameActive = true
            startTime = SystemClock.elapsedRealtime()
          } else {
            Toast.makeText(context, "Invalid round information in challenge", Toast.LENGTH_SHORT)
                .show()
          }
        },
        onFailure = {
          Toast.makeText(context, "Failed to fetch challenge", Toast.LENGTH_SHORT).show()
        })
  }

  // Real-time update of the elapsed time when the game is active
  LaunchedEffect(isGameActive) {
    if (isGameActive) {
      while (isGameActive) {
        elapsedTime = SystemClock.elapsedRealtime() - startTime
        delay(100)
      }
    }
  }

  // Gesture Recognition Updates
  val landmarksState by handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  LaunchedEffect(landmarksState) {
    if (!landmarksState.isNullOrEmpty() && isGameActive) {
      val currentLetter = currentWord.getOrNull(currentLetterIndex)?.uppercaseChar()
      if (currentLetter != null &&
          detectedGesture.equals(currentLetter.toString(), ignoreCase = true)) {
        currentLetterIndex++
        if (currentLetterIndex <= currentWord.length) {
          typedWord += currentLetter
        }

        if (typedWord.length == currentWord.length) {
          isGameActive = false
          elapsedTime = SystemClock.elapsedRealtime() - startTime
          onWordCompletion(
              context,
              currentUserId,
              challengeRepository,
              currentChallenge,
              elapsedTime,
              navigationActions)
        }
      }
    }
  }

  // If current challenge is null, show loading text
  if (currentChallenge == null) {
    DisplayLoadingText()
    return
  }

  // If the challenge is completed, show completed message
  if (currentChallenge?.gameStatus == "completed") {
    DisplayChallengeCompletedText()
    return
  }

  // Render the challenge screen
  AnnexScreenScaffold(
      navigationActions = navigationActions, testTagColumn = "ChronoChallengeScreen") {
        if (currentWord.isNotEmpty()) {
          ChronoChallengeContent(
              elapsedTime = elapsedTime,
              currentWord = currentWord,
              currentLetterIndex = currentLetterIndex,
              handLandMarkViewModel = handLandMarkViewModel,
          )
        } else {
          // If no words are available, show appropriate message
          Text(
              "No word available for this round.",
              modifier = Modifier.testTag("NoWordAvailableText"))
        }
      }
}

@Composable
fun ChronoChallengeContent(
    elapsedTime: Long,
    currentWord: String,
    currentLetterIndex: Int,
    handLandMarkViewModel: HandLandMarkViewModel
) {
  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp).testTag("ChronoChallengeContent"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top) {
        Chronometer(elapsedTime)
        Spacer(modifier = Modifier.height(32.dp))

        CurrentLetterDisplay(currentWord, currentLetterIndex)
        SentenceLayerDisplay(currentWord, currentLetterIndex)
        CameraPlaceholder(handLandMarkViewModel, Modifier.testTag("CameraPlaceholder"))
      }
}

@Composable
fun Chronometer(elapsedTime: Long) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("ChronometerRow"),
      horizontalArrangement = Arrangement.End) {
        Text(
            text = "Time: ${elapsedTime / 1000}s",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp).testTag("ElapsedTimeText"))
      }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CurrentLetterDisplay(currentWord: String, currentLetterIndex: Int) {
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
                .testTag("CurrentLetterBox"),
        contentAlignment = Alignment.Center) {
          Icon(
              painter = painterResource(id = imageResId),
              contentDescription = "Sign image",
              tint = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.size(120.dp).testTag("CurrentLetterIcon"))
        }
    Spacer(modifier = Modifier.height(16.dp))
  }
}

@Composable
fun SentenceLayerDisplay(currentWord: String, currentLetterIndex: Int) {
  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 16.dp)
              .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
              .testTag("SentenceLayerBox")) {
        Text(
            text = buildForegroundText(currentWord, currentLetterIndex),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.align(Alignment.Center).testTag("CurrentWordText"))
      }
}

@Composable
fun DisplayLoadingText() {
  Text("Loading challenge...", modifier = Modifier.testTag("LoadingChallengeText"))
}

@Composable
fun DisplayChallengeCompletedText() {
  Text(
      "Challenge Completed! Go to History to see the results.",
      modifier = Modifier.testTag("ChallengeCompletedText"))
}

@Composable
fun buildForegroundText(currentWord: String, currentLetterIndex: Int): AnnotatedString {
  val clampedIndex = currentLetterIndex.coerceAtMost(currentWord.length - 1)

  return buildAnnotatedString {
    append(currentWord.substring(0, clampedIndex)) // Before the current letter
    if (clampedIndex < currentWord.length) {
      withStyle(
          style =
              SpanStyle(
                  letterSpacing = 10.sp,
                  color = MaterialTheme.colorScheme.secondary,
                  fontSize = 50.sp)) {
            append(currentWord[clampedIndex].uppercase()) // Current letter in uppercase
      }
    }
    if (clampedIndex + 1 < currentWord.length) {
      append(currentWord.substring(clampedIndex + 1)) // After the current letter
    }
  }
}

fun onWordCompletion(
    context: Context,
    currentUserId: String,
    challengeRepository: ChallengeRepository,
    challenge: Challenge?,
    elapsedTime: Long,
    navigationActions: NavigationActions
) {
  challenge?.let {
    val updatedChallenge =
        it.copy(
            gameStatus = if (it.round == 3) "completed" else "in_progress",
            round = it.round + 1,
            player1RoundCompleted =
                it.player1RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player1) this[it.round - 1] = true
                },
            player2RoundCompleted =
                it.player2RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player2) this[it.round - 1] = true
                },
            player1Times =
                it.player1Times.toMutableList().apply {
                  if (currentUserId == it.player1) add(elapsedTime)
                },
            player2Times =
                it.player2Times.toMutableList().apply {
                  if (currentUserId == it.player2) add(elapsedTime)
                })
    challengeRepository.updateChallenge(
        updatedChallenge = updatedChallenge,
        onSuccess = {
          Toast.makeText(context, "Score saved successfully", Toast.LENGTH_SHORT).show()
          navigationActions.navigateTo(Screen.CHALLENGE)
        },
        onFailure = { Toast.makeText(context, "Failed to save score", Toast.LENGTH_SHORT).show() })
  }
}
