package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun ExerciseScreenHard(navigationActions: NavigationActions) {

  // SuccessButton, CameraPlaceholder, WordLayer Composables are defined in PracticeScreenEasy
  // Another file could be made but overkill

  val context = LocalContext.current

  val word1 = "FGYFFG"
  val word2 = "FGYGFG"
  val word3 = "FGYYFG"

  val words = listOf(word1, word2, word3).map { it.lowercase() }

  // MutableState to keep track of the current letter
  var currentWordIndex by rememberSaveable { mutableIntStateOf(0) }

  // MutableState to keep track of the current letter
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }

  Box(
      modifier = Modifier.fillMaxSize().background(Color.White),
      contentAlignment = Alignment.Center // Center all content in the Box
      ) {
        IconButton(
            onClick = { navigationActions.goBack() },
            modifier =
                Modifier.padding(16.dp) // Add some padding to position it away from the edges
                    .align(Alignment.TopStart)
                    .testTag("Back")) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = Color.Black // Customize icon color if needed
                  )
            }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content in the Column as well
            ) {
              WordLayer(words, currentWordIndex, currentLetterIndex)

              CameraPlaceholder()
            }

        SuccessButton(
            currentLetterIndex = currentLetterIndex,
            currentWordIndex = currentWordIndex,
            words = words,
            onNextLetter = { nextIndex -> currentLetterIndex = nextIndex },
            onNextWord = { nextWordIndex ->
              currentWordIndex = nextWordIndex
              currentLetterIndex = 0
            },
            onAllWordsComplete = {
              Toast.makeText(context, "Words Completed!", Toast.LENGTH_SHORT).show()
            })
      }
}
