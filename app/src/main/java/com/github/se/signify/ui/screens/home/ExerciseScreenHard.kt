package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.navigation.NavigationActions

/**
 * Composable function for the Hard Level Exercise Screen. This screen allows the user to practice
 * recognizing letters in American Sign Language (ASL) by using the hand landmarks detected by the
 * device's camera.
 *
 * @param navigationActions A collection of navigation actions that can be triggered from this
 *   screen.
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 */
@Composable
fun ExerciseScreenHard(
    navigationActions: NavigationActions,
    handLandMarkViewModel: HandLandMarkViewModel
) {
  val context = LocalContext.current
  val realWords = stringArrayResource(R.array.real_words_hard).toList()

  // Placeholders, eventually have a viewmodel to pass arguments for the list of words.
  var words by rememberSaveable { mutableStateOf(List(3) { realWords.random() }) }
  val word1 = words[0]
  val word2 = words[1]
  val word3 = words[2]

  val wordsList = listOf(word1, word2, word3).map { it.lowercase() }
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentWordIndex by rememberSaveable { mutableIntStateOf(0) }
  val currentLetter = wordsList[currentWordIndex][currentLetterIndex]

  val landmarksState = handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  if (!landmarksState.value.isNullOrEmpty()) {
    handleGestureMatching(
        detectedGesture = detectedGesture,
        currentLetter = currentLetter,
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        wordsList = wordsList,
        onNextLetter = { nextIndex -> currentLetterIndex = nextIndex },
        onNextWord = { nextWordIndex ->
          currentWordIndex = nextWordIndex
          currentLetterIndex = 0
        },
        onAllWordsComplete = {
          Toast.makeText(context, "Words Completed!", Toast.LENGTH_SHORT).show()
          words =
              List(3) {
                realWords.filter { it.length in 5..7 }.random()
              } // Reset with new random words
          currentWordIndex = 0
          currentLetterIndex = 0
        })
  }

  LazyColumn(
      modifier =
          Modifier.fillMaxSize()
              .background(colorResource(R.color.white))
              .testTag("ExerciseScreenHard"),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        // Back button
        item {
          IconButton(
              onClick = { navigationActions.goBack() },
              modifier = Modifier.padding(16.dp).testTag("Back")) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(R.color.black))
              }
        }

        // Display sign image for current letter
        item {
          val imageName = "letter_${currentLetter}"
          val imageResId =
              context.resources.getIdentifier(imageName, "drawable", context.packageName)

          if (imageResId != 0) {
            Box(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(150.dp)
                        .background(colorResource(R.color.blue), shape = RoundedCornerShape(16.dp))
                        .border(
                            2.dp, colorResource(R.color.black), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center) {
                  Image(
                      painter = painterResource(id = imageResId),
                      contentDescription = "Sign image",
                      modifier = Modifier.size(120.dp))
                }

            Spacer(modifier = Modifier.height(16.dp))
          } else {
            Text("Image for letter $currentLetter not found.")
          }
        }

        // Word layer display
        item { WordLayer(wordsList, currentWordIndex, currentLetterIndex) }

        // Camera placeholder/composable
        item { CameraPlaceholder(handLandMarkViewModel) }
      }
}
