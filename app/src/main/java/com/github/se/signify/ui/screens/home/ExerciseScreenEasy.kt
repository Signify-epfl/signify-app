package com.github.se.signify.ui.screens.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.hand.HandLandMarkViewModel
import com.github.se.signify.ui.AnnexScreenScaffold
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
 * Manages progression through letters, words, and sentences in a text-based exercise.
 *
 * This function is called when a "success" action occurs, such as a correctly detected gesture or
 * input matching the current letter. It updates the indices for the current letter, word, or
 * sentence and triggers the appropriate callback based on the progression.
 *
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence within the list of sentences.
 * @param sentences The list of sentences used in the exercise.
 * @param onNextLetter Callback to trigger the next letter progression.
 * @param onNextWord Callback to trigger the next word progression, resetting the letter index.
 * @param onNextSentence Callback to trigger the next sentence progression, resetting the word and
 *   letter indices.
 * @param onAllSentencesComplete Callback to be called when all sentences are completed.
 */
fun onSuccess(
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int,
    sentences: List<String>,
    onNextLetter: (Int) -> Unit,
    onNextWord: (Int) -> Unit,
    onNextSentence: (Int) -> Unit,
    onAllSentencesComplete: () -> Unit
) {
  // Retrieve the current sentence and split it into words
  val currentSentence = sentences[currentSentenceIndex]
  val words = currentSentence.split(" ")
  val currentWord = words[currentWordIndex]

  if (currentLetterIndex < currentWord.length - 1) {
    // Move to the next letter within the current word
    onNextLetter(currentLetterIndex + 1)
  } else {
    // If at the end of the current word, reset letter index and move to the next word
    if (currentWordIndex < words.size - 1) {
      // Move to the next word in the sentence
      onNextWord(currentWordIndex + 1)
      onNextLetter(0) // Reset to the first letter of the new word
    } else {
      // If at the end of the sentence, reset indices and move to the next sentence
      if (currentSentenceIndex < sentences.size - 1) {
        onNextSentence(currentSentenceIndex + 1)
        onNextWord(0) // Start at the first word of the new sentence
        onNextLetter(0) // Start at the first letter of the first word
      } else {
        // If all sentences are completed, invoke the completion callback
        onAllSentencesComplete()
      }
    }
  }
}

/**
 * Composable function to display the current sentence in a semi-transparent back layer and the
 * current word in a highlighted front layer with letter emphasis.
 *
 * The back layer displays the entire current sentence, with the current word visually highlighted.
 * If the current sentence contains only one word, the next sentence is displayed instead, without
 * any highlights, to maintain visual continuity. The front layer displays the current word
 * prominently, with emphasis on the current letter.
 *
 * @param sentences The list of sentences for the exercise.
 * @param currentSentenceIndex The index of the current sentence in the list.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentLetterIndex The index of the current letter within the current word.
 */
@Composable
fun SentenceLayer(
    sentences: List<String>,
    currentSentenceIndex: Int,
    currentWordIndex: Int,
    currentLetterIndex: Int
) {
  // Ensure the current sentence index is within bounds
  if (currentSentenceIndex >= sentences.size) return

  // Get the current sentence and split it into words
  val currentSentence = sentences[currentSentenceIndex]
  val wordsInSentence = currentSentence.split(" ")

  // Ensure the current word index is within bounds of the words in the sentence
  if (currentWordIndex >= wordsInSentence.size) return

  // Get the current word for the primary layer
  val currentWord = wordsInSentence[currentWordIndex]

  // Determine if this sentence has only one word
  val isSingleWordSentence = wordsInSentence.size == 1
  // Get the next sentence if available
  val nextSentence =
      if (currentSentenceIndex + 1 < sentences.size) sentences[currentSentenceIndex + 1] else ""

  Box(
      modifier =
          Modifier.fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 16.dp)
              .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
              .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp))
              .testTag("sentenceLayer"),
      contentAlignment = Alignment.Center) {
        Text(
            text =
                buildAnnotatedString {
                  if (isSingleWordSentence && nextSentence.isNotEmpty()) {
                    append(nextSentence.lowercase()) // Display the next sentence without highlights
                  } else {
                    wordsInSentence.forEachIndexed { index, word ->
                      if (index == currentWordIndex) {
                        // Apply highlighting to the current word
                        withStyle(
                            style =
                                SpanStyle(
                                    color = MaterialTheme.colorScheme.secondary, // Highlight color
                                    fontWeight = FontWeight.Bold)) {
                              append(word)
                            }
                      } else {
                        append(word)
                      }
                      if (index < wordsInSentence.size - 1) {
                        append(" ") // Add space between words
                      }
                    }
                  }
                },
            modifier = Modifier.offset(y = (-40).dp).testTag("FullSentenceTag"),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
            style = TextStyle(fontSize = 24.sp))

        // Front layer: Current word with highlighted letter
        Text(
            buildAnnotatedString {
              append(currentWord.substring(0, currentLetterIndex)) // Before the current letter

              // Highlight the current letter
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
            modifier = Modifier.testTag("CurrentWordTag"),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSecondary)
      }
}

/**
 * Function to handle the gesture matching logic.
 *
 * This function compares the detected gesture with the current letter and, if they match, calls the
 * `onSuccess` function to handle progression through letters, words, and sentences. If they do not
 * match, a log message is printed.
 *
 * @param detectedGesture The gesture detected by the hand landmark model as a string.
 * @param currentLetter The current letter to be matched.
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence in the list of sentences.
 * @param sentencesList The list of sentences used in the exercise.
 * @param onNextLetter Callback to move to the next letter in the current word.
 * @param onNextWord Callback to move to the next word in the current sentence.
 * @param onNextSentence Callback to move to the next sentence in the list.
 * @param onAllSentencesComplete Callback to handle when all sentences are completed.
 */
fun handleGestureMatching(
    detectedGesture: String,
    currentLetter: Char,
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int,
    sentencesList: List<String>,
    onNextLetter: (Int) -> Unit,
    onNextWord: (Int) -> Unit,
    onNextSentence: (Int) -> Unit,
    onAllSentencesComplete: () -> Unit
) {
  if (detectedGesture == currentLetter.uppercase()) {
    onSuccess(
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        currentSentenceIndex = currentSentenceIndex,
        sentences = sentencesList,
        onNextLetter = onNextLetter,
        onNextWord = onNextWord,
        onNextSentence = onNextSentence,
        onAllSentencesComplete = onAllSentencesComplete)
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
  val sentencesList by rememberSaveable {
    mutableStateOf(List(3) { realWords.filter { wordFilter?.invoke(it) ?: true }.random() })
  }

  var currentSentenceIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentWordIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }

  // Retrieve the current sentence, word, and letter
  val currentSentence = sentencesList[currentSentenceIndex]
  val wordsInCurrentSentence = currentSentence.split(" ")
  val currentWord = wordsInCurrentSentence[currentWordIndex]
  val currentLetter = currentWord[currentLetterIndex].lowercaseChar()

  val landmarksState = handLandMarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandMarkViewModel.getSolution()

  if (!landmarksState.value.isNullOrEmpty()) {
    handleGestureMatching(
        detectedGesture = detectedGesture,
        currentLetter = currentLetter,
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        currentSentenceIndex = currentSentenceIndex,
        sentencesList = sentencesList,
        onNextLetter = { nextIndex -> currentLetterIndex = nextIndex },
        onNextWord = { nextWordIndex ->
          currentWordIndex = nextWordIndex
          currentLetterIndex = 0
        },
        onNextSentence = { nextSentenceIndex ->
          currentSentenceIndex = nextSentenceIndex
          currentWordIndex = 0
          currentLetterIndex = 0
        },
        onAllSentencesComplete = {
          Toast.makeText(context, "Exercise completed!", Toast.LENGTH_SHORT).show()
          // Reload or reset sentencesList, or handle end of exercise as needed
          currentSentenceIndex = 0
          currentWordIndex = 0
          currentLetterIndex = 0
        })
  }

  AnnexScreenScaffold(navigationActions = navigationActions, testTagColumn = screenTag) {

    // Display sign image for the current letter
    val imageName = "letter_${currentLetter.lowercase()}"
    val imageResId = context.resources.getIdentifier(imageName, "drawable", context.packageName)

    if (imageResId != 0) {
      Box(
          modifier =
              Modifier.fillMaxWidth()
                  .padding(horizontal = 16.dp)
                  .height(150.dp)
                  .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp))
                  .border(
                      2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)),
          contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Sign image",
                modifier = Modifier.size(120.dp))
          }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Sentence layer display
    SentenceLayer(sentencesList, currentSentenceIndex, currentWordIndex, currentLetterIndex)

    // Camera placeholder/composable
    CameraPlaceholder(handLandMarkViewModel)
  }
}
