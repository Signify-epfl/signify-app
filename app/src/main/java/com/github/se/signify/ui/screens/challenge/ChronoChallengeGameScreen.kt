package com.github.se.signify.ui.screens.challenge

import android.os.SystemClock
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.model.auth.UserSession
import com.github.se.signify.model.challenge.Challenge
import com.github.se.signify.model.challenge.ChallengeRepository
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.navigation.NavigationActions
import com.github.se.signify.ui.navigation.Screen
import com.github.se.signify.ui.screens.home.SentenceLayer

@Composable
fun ChronoChallengeGameScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
    challengeRepository: ChallengeRepository,
    handLandMarkViewModel: HandLandMarkViewModel = viewModel(),
    challengeId: String
) {
    val context = LocalContext.current
    val currentUserId = userSession.getUserId() ?: return

    // Mutable state for tracking challenge information
    var currentChallenge by remember { mutableStateOf<Challenge?>(null) }
    var isPlayerTurn by remember { mutableStateOf(false) }
    var currentWord by remember { mutableStateOf("") }

    // Fetch the current challenge
    LaunchedEffect(Unit) {
        challengeRepository.getChallengeById(
            challengeId = challengeId,
            onSuccess = {
                currentChallenge = it
                currentWord = it.roundWords[it.round - 1]
                isPlayerTurn = if (currentUserId == it.player1) {
                    !it.player1RoundCompleted[it.round - 1]
                } else {
                    !it.player2RoundCompleted[it.round - 1]
                }
            },
            onFailure = {
                Toast.makeText(context, "Failed to fetch challenge", Toast.LENGTH_SHORT).show()
            }
        )
    }

    var currentLetterIndex by remember { mutableIntStateOf(0) }
    var typedWord by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf(0L) }
    var elapsedTime by remember { mutableStateOf(0L) }

    if (currentChallenge == null) {
        Text("Loading challenge...")
        return
    }

    if (currentChallenge?.gameStatus == "completed") {
        Text("Challenge Completed! Go to History to see the results.")
        return
    }

    if (!isPlayerTurn) {
        Text("Waiting for the other player to complete their round.")
        return
    }

    AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = "ChronoChallengeScreen") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display the current letter's icon
            val currentLetter = currentWord.getOrNull(currentLetterIndex)?.uppercaseChar() ?: 'A'
            val imageName = "letter_${currentLetter.lowercase()}"
            val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

            if (imageResId != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                        .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Sign image",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(120.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sentence layer display
            SentenceLayer(
                sentences = listOf(currentWord),
                currentLetterIndex = currentLetterIndex,
                currentWordIndex = 0,
                currentSentenceIndex = 0
            )

            // Start the chronometer if not already started
            if (startTime == 0L) {
                startTime = SystemClock.elapsedRealtime()
            }

            // Recognize gestures using HandLandMarkViewModel and update the current letter
            val recognizedLetter = handLandMarkViewModel.getSolution() // Directly retrieve the recognized letter

            // Check and update the current letter if it matches the recognized letter
            if (recognizedLetter.isNotEmpty() && recognizedLetter.equals(currentLetter.toString(), ignoreCase = true)) {
                currentLetterIndex++
                typedWord += recognizedLetter
            }

            // Automatically submit when the word matches the current word
            if (typedWord == currentWord) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                currentChallenge?.let {
                    val updatedChallenge = it.copy(
                        gameStatus = if (it.round == 3) "completed" else "in_progress",
                        round = it.round + 1,
                        player1RoundCompleted = it.player1RoundCompleted.toMutableList().apply {
                            if (currentUserId == it.player1) this[it.round - 1] = true
                        },
                        player2RoundCompleted = it.player2RoundCompleted.toMutableList().apply {
                            if (currentUserId == it.player2) this[it.round - 1] = true
                        },
                        player1Times = it.player1Times.toMutableList().apply {
                            if (currentUserId == it.player1) add(elapsedTime)
                        },
                        player2Times = it.player2Times.toMutableList().apply {
                            if (currentUserId == it.player2) add(elapsedTime)
                        }
                    )
                    challengeRepository.updateChallenge(
                        updatedChallenge = updatedChallenge,
                        onSuccess = {
                            Toast.makeText(context, "Score saved successfully", Toast.LENGTH_SHORT).show()
                            navigationActions.navigateTo(Screen.CHALLENGE_HISTORY)
                        },
                        onFailure = {
                            Toast.makeText(context, "Failed to save score", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Camera placeholder/composable for ASL recognition
            CameraPlaceholder(handLandMarkViewModel)
        }
    }
}
