package com.github.se.signify.ui.screens.challenge

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
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
fun ChronoChallengeGameScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    challengeRepository: ChallengeRepository,
    handLandmarkViewModel: HandLandmarkViewModel,
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
  val invalidChallengeText = stringResource(R.string.invalid_challenge_text)
  val failedFetchText = stringResource(R.string.failed_fetch_text)
  // Fetch the current challenge
  LaunchedEffect(challengeId) {
    challengeRepository.getChallengeById(
        challengeId = challengeId,
        onSuccess = { challenge ->
          currentChallenge = challenge
          if (challenge.roundWords.isNotEmpty() && challenge.round > 0 && challenge.round <= 6) {
            currentWord = challenge.roundWords[findNextIndex(challenge, currentUserId)]
            currentLetterIndex = 0
            typedWord = ""
            isGameActive = true
            startTime = SystemClock.elapsedRealtime()
          } else {
            Toast.makeText(context, invalidChallengeText, Toast.LENGTH_SHORT).show()
          }
        },
        onFailure = { Toast.makeText(context, failedFetchText, Toast.LENGTH_SHORT).show() })
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
  val landmarksState by handLandmarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandmarkViewModel.getSolution()
  val scoreSavedText = stringResource(R.string.score_saved_text)
  val scoreFailedText = stringResource(R.string.score_failed_text)

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
              navigationActions,
              scoreSavedText,
              scoreFailedText)
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
      navigationActions = navigationActions,
      testTag = "ChronoChallengeScreen",
  ) {
    Spacer(modifier = Modifier.height(32.dp))

    if (currentWord.isNotEmpty()) {
      ChronoChallengeContent(
          elapsedTime = elapsedTime,
          currentWord = currentWord,
          currentLetterIndex = currentLetterIndex,
          handLandmarkViewModel = handLandmarkViewModel,
      )
    } else {
      // If no words are available, show appropriate message
      val noWordAvailableText = stringResource(R.string.no_word_available_text)
      Text(noWordAvailableText, modifier = Modifier.testTag("NoWordAvailableText"))
    }
  }
}

@Composable
fun ChronoChallengeContent(
    elapsedTime: Long,
    currentWord: String,
    currentLetterIndex: Int,
    handLandmarkViewModel: HandLandmarkViewModel
) {
  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp).testTag("ChronoChallengeContent"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top) {
        Chronometer(elapsedTime)
        Spacer(modifier = Modifier.height(32.dp))

        CurrentLetterDisplay(currentWord, currentLetterIndex)
        SentenceLayerDisplay(currentWord, currentLetterIndex)
        CameraBox(handLandmarkViewModel = handLandmarkViewModel, testTag = "CameraBox")
      }
}

@Composable
fun Chronometer(elapsedTime: Long) {
  Row(
      modifier = Modifier.fillMaxWidth().testTag("ChronometerRow"),
      horizontalArrangement = Arrangement.End) {
        val timeText = stringResource(R.string.time_text)
        Text(
            text = "$timeText ${elapsedTime / 1000}s",
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
  val loadingText = stringResource(R.string.loading_text)
  Text(loadingText, modifier = Modifier.testTag("LoadingChallengeText"))
}

@Composable
fun DisplayChallengeCompletedText() {
  val challengeDoneText = stringResource(R.string.challenge_done_text)
  Text(challengeDoneText, modifier = Modifier.testTag("ChallengeCompletedText"))
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
    navigationActions: NavigationActions,
    scoreSavedText: String = "",
    scoreFailedText: String = ""
) {
  challenge?.let {
    val nextRoundIndex = findNextIndex(it, currentUserId)

    val updatedChallenge =
        it.copy(
            gameStatus = if (it.round == 6) "completed" else "in_progress",
            round = it.round + 1,
            player1RoundCompleted =
                it.player1RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player1 && nextRoundIndex != -1)
                      this[nextRoundIndex] = true
                },
            player2RoundCompleted =
                it.player2RoundCompleted.toMutableList().apply {
                  if (currentUserId == it.player2 && nextRoundIndex != -1)
                      this[nextRoundIndex] = true
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
          Toast.makeText(context, scoreSavedText, Toast.LENGTH_SHORT).show()
          navigationActions.navigateTo(Screen.CHALLENGE)
        },
        onFailure = { Toast.makeText(context, scoreFailedText, Toast.LENGTH_SHORT).show() })
  }
}

fun findNextIndex(challenge: Challenge, currentUserId: String): Int {
  return when (currentUserId) {
    challenge.player1 -> {
      challenge.player1RoundCompleted.indexOfFirst {
        !it
      } // Find the first incomplete round for player1
    }
    challenge.player2 -> {
      challenge.player2RoundCompleted.indexOfFirst {
        !it
      } // Find the first incomplete round for player2
    }
    else -> 0 // Return 0 if the user is neither player1 nor player2
  }
}
