package com.github.se.signify.ui.screens.challenge

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
  LaunchedEffect(Unit) {
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

  // Real-time update of the elapsed time
  LaunchedEffect(isGameActive) {
    if (isGameActive) {
      while (isGameActive) {
        elapsedTime = SystemClock.elapsedRealtime() - startTime
        delay(100) // Update every 100ms
      }
    }
  }

  // Real-time gesture recognition updates
  val landmarksState by handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  LaunchedEffect(landmarksState) {
    if (!landmarksState.isNullOrEmpty() && isGameActive) {
      val currentLetter = currentWord.getOrNull(currentLetterIndex)?.uppercaseChar()
      if (currentLetter != null &&
          detectedGesture.isNotEmpty() &&
          detectedGesture.equals(currentLetter.toString(), ignoreCase = true)) {
        currentLetterIndex++
        if (currentLetterIndex <= currentWord.length) {
          typedWord += currentLetter
        }

        // Handle word completion safely
        if (typedWord.length == currentWord.length) {
          isGameActive = false
          elapsedTime = SystemClock.elapsedRealtime() - startTime
          onWordCompletion(
              context = context,
              currentUserId = currentUserId,
              challengeRepository = challengeRepository,
              challenge = currentChallenge,
              elapsedTime = elapsedTime,
              navigationActions = navigationActions)
        }
      }
    }
  }

  if (currentChallenge == null) {
    Text("Loading challenge...")
    return
  }

  if (currentChallenge?.gameStatus == "completed") {
    Text("Challenge Completed! Go to History to see the results.")
    return
  }

  // UI for the Chrono Challenge Game
  AnnexScreenScaffold(
      navigationActions = navigationActions, testTagColumn = "ChronoChallengeScreen") {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
              // Chronometer in the top-right corner
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "Time: ${elapsedTime / 1000}s",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(8.dp))
              }

              Spacer(modifier = Modifier.height(32.dp))

              if (currentWord.isNotEmpty()) {
                // Display the current letter's icon
                val currentLetter =
                    currentWord.getOrNull(currentLetterIndex)?.uppercaseChar() ?: 'A'
                val imageName = "letter_${currentLetter.lowercase()}"
                val imageResId =
                    context.resources.getIdentifier(imageName, "drawable", context.packageName)

                if (imageResId != 0) {
                  Box(
                      modifier =
                          Modifier.fillMaxWidth()
                              .padding(horizontal = 16.dp)
                              .height(150.dp)
                              .background(
                                  MaterialTheme.colorScheme.primary,
                                  shape = RoundedCornerShape(16.dp))
                              .border(
                                  2.dp,
                                  MaterialTheme.colorScheme.outline,
                                  shape = RoundedCornerShape(16.dp)),
                      contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Sign image",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(120.dp))
                      }

                  Spacer(modifier = Modifier.height(16.dp))
                }

                // Sentence layer display with real-time foreground highlighting using
                // buildForegroundText
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 16.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp))
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(16.dp))) {
                      Text(
                          text = buildForegroundText(currentWord, currentLetterIndex),
                          style = TextStyle(fontSize = 30.sp),
                          color = MaterialTheme.colorScheme.onSecondary,
                          modifier = Modifier.align(Alignment.Center))
                    }

                Spacer(modifier = Modifier.height(16.dp))

                // Camera placeholder/composable for ASL recognition
                CameraPlaceholder(handLandMarkViewModel)
              } else {
                Text("No word available for this round.")
              }
            }
      }
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
    context: android.content.Context,
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
