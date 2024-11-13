package com.github.se.signify.ui.screens.home

import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.CameraPlaceholder
import com.github.se.signify.ui.navigation.NavigationActions

/**
 * Composable function for the Easy Level Exercise Screen. This screen allows the user to practice
 * recognizing letters in American Sign Language (ASL) using the hand landmarks detected by the
 * device's camera.
 *
 * @param navigationActions A collection of navigation actions that can be triggered from this
 *   screen.
 * @param handLandMarkViewModel The ViewModel responsible for managing hand landmark detection.
 */
@Composable
fun ExerciseScreenEasy(
    navigationActions: NavigationActions,
    handLandMarkViewModel: HandLandMarkViewModel
) {
  ExerciseScreenCommon(
      navigationActions = navigationActions,
      handLandMarkViewModel = handLandMarkViewModel,
      wordsResourceId = R.array.real_words,
      screenTag = "ExerciseScreenEasy")
}

/**
 * Handles the successful recognition of a letter during the exercise. This function manages
 * progressing to the next letter, word, or completing the exercise.
 *
 * @param currentLetterIndex The index of the current letter in the word.
 * @param currentWordIndex The index of the current word in the list.
 * @param words The list of words used in the exercise.
 * @param onNextLetter Callback to move to the next letter in the current word.
 * @param onNextWord Callback to move to the next word in the list.
 * @param onAllWordsComplete Callback to handle when all words are completed.
 */
fun onSuccess(
    currentLetterIndex: Int,
    currentWordIndex: Int,
    words: List<String>,
    onNextLetter: (Int) -> Unit,
    onNextWord: (Int) -> Unit,
    onAllWordsComplete: () -> Unit
) {
  val currentWord = words[currentWordIndex]

  if (currentLetterIndex < currentWord.length - 1) {
    // Move to the next letter in the current word
    onNextLetter(currentLetterIndex + 1)
  } else {
    // If at the end of the current word, move to the next word
    if (currentWordIndex < words.size - 1) {
      // Move to the next word
      onNextWord(currentWordIndex + 1)
    } else {
      // If at the end of the word list, mark the task as complete
      onAllWordsComplete()
    }
  }
}

/**
 * Composable function to display the current and next words in the exercise. The current word is
 * highlighted with the current letter emphasized.
 *
 * @param words The list of words used in the exercise.
 * @param currentWordIndex The index of the current word.
 * @param currentLetterIndex The index of the current letter in the current word.
 */
@Composable
fun WordLayer(words: List<String>, currentWordIndex: Int, currentLetterIndex: Int) {
  if (currentWordIndex >= words.size) return

  val currentWord = words[currentWordIndex]
  val nextWord = if (currentWordIndex + 1 < words.size) words[currentWordIndex + 1] else ""

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 16.dp)
              .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
              .testTag("wordLayer"),
      contentAlignment = Alignment.Center) {
        // Next word (semi-transparent, slightly offset upwards)
        if (nextWord.isNotEmpty()) {
          Text(
              text = nextWord,
              modifier = Modifier.offset(y = (-40).dp).testTag("SecondWordTag"),
              color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
              style = TextStyle(fontSize = 30.sp))
        }

        // Current word (fully visible)
        Text(
            buildAnnotatedString {
              append(currentWord.substring(0, currentLetterIndex)) // Before the current letter

              withStyle(
                  style =
                      SpanStyle(
                          letterSpacing = 10.sp,
                          color = MaterialTheme.colorScheme.secondary, // Highlight color
                          fontSize = 50.sp // Larger size for the current letter
                          )) {
                    append(
                        currentWord[currentLetterIndex].uppercase()) // Current letter in uppercase
              }

              append(currentWord.substring(currentLetterIndex + 1)) // After the current letter
            },
            modifier = Modifier.testTag("FirstWordTag"),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSecondary)
      }
}

/**
 * Function to handle the gesture matching logic.
 *
 * @param detectedGesture The gesture detected by the hand landmark model.
 * @param currentLetter The current letter to be matched.
 * @param currentLetterIndex The index of the current letter in the current word.
 * @param currentWordIndex The index of the current word in the list.
 * @param wordsList The list of words used in the exercise.
 * @param onNextLetter Callback to move to the next letter in the current word.
 * @param onNextWord Callback to move to the next word in the list.
 * @param onAllWordsComplete Callback to handle when all words are completed.
 */
fun handleGestureMatching(
    detectedGesture: String,
    currentLetter: Char,
    currentLetterIndex: Int,
    currentWordIndex: Int,
    wordsList: List<String>,
    onNextLetter: (Int) -> Unit,
    onNextWord: (Int) -> Unit,
    onAllWordsComplete: () -> Unit
) {
  if (detectedGesture == currentLetter.uppercase()) {
    onSuccess(
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        words = wordsList,
        onNextLetter = onNextLetter,
        onNextWord = onNextWord,
        onAllWordsComplete = onAllWordsComplete)
  } else {
    Log.d(
        "ExerciseScreenEasy",
        "Detected gesture ($detectedGesture) does not match the current letter ($currentLetter)")
  }
}

@Composable
fun ExerciseScreenCommon(
    navigationActions: NavigationActions,
    handLandMarkViewModel: HandLandMarkViewModel,
    wordsResourceId: Int,
    screenTag: String,
    wordFilter: ((String) -> Boolean)? = null
) {
  val context = LocalContext.current
  val realWords = stringArrayResource(wordsResourceId).toList()
  var words by rememberSaveable {
    mutableStateOf(List(3) { realWords.filter { wordFilter?.invoke(it) ?: true }.random() })
  }
  val wordsList = words.map { it.lowercase() }

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
          words = List(3) { realWords.filter { wordFilter?.invoke(it) ?: true }.random() }
          currentWordIndex = 0
          currentLetterIndex = 0
        })
  }

  LazyColumn(
      modifier =
          Modifier.fillMaxSize()
              .background(MaterialTheme.colorScheme.background)
              .testTag(screenTag),
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
                    tint = MaterialTheme.colorScheme.onBackground)
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
                        .background(
                            MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center) {
                  Image(
                      painter = painterResource(id = imageResId),
                      contentDescription = "Sign image",
                      modifier = Modifier.size(120.dp))
                }

            Spacer(modifier = Modifier.height(16.dp))
          }
        }

        // Word layer display
        item { WordLayer(wordsList, currentWordIndex, currentLetterIndex) }

        // Camera placeholder/composable
        item { CameraPlaceholder(handLandMarkViewModel) }
      }
}
