package com.github.se.signify.ui.screens.home

import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.home.exercise.ExerciseLevel
import com.github.se.signify.model.home.exercise.ExerciseLevelName
import com.github.se.signify.model.home.exercise.ExerciseTrackTime
import com.github.se.signify.model.home.hand.HandLandmarkViewModel
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.profile.stats.StatsRepository
import com.github.se.signify.model.profile.stats.StatsViewModel
import com.github.se.signify.ui.common.AnnexScreenScaffold
import com.github.se.signify.ui.common.CameraBox

/**
 * Composable function for a common exercise screen layout, handling gesture detection, sentence
 * progression, and displaying the current sign image based on letter recognition.
 *
 * This function displays the current letter from a list of sentences, updates the current word and
 * letter when gestures are correctly matched.When all sentences are completed, it resets and
 * displays a completion message.
 *
 * @param navigationActions Provides navigation actions for the screen.
 * @param handLandmarkViewModel ViewModel responsible for managing hand landmark detection and
 *   gesture matching.
 * @param exerciseLevel Provides complementary information about the level.
 */
@Composable
fun ExerciseScreen(
    navigationActions: NavigationActions,
    handLandmarkViewModel: HandLandmarkViewModel,
    userSession: UserSession,
    statsRepository: StatsRepository,
    exerciseLevel: ExerciseLevel
) {
  val statsViewModel: StatsViewModel =
      viewModel(factory = StatsViewModel.factory(userSession, statsRepository))

  val context = LocalContext.current
  val realSentences = stringArrayResource(exerciseLevel.wordsResourceId).toList()
  val sentencesList by rememberSaveable {
    mutableStateOf(
        List(3) { realSentences.filter { exerciseLevel.wordFilter?.invoke(it) ?: true }.random() })
  }

  val trackTime = ExerciseTrackTime(statsViewModel = statsViewModel)

  var currentSentenceIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentWordIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }

  val currentLetter =
      getCurrentLetter(sentencesList, currentLetterIndex, currentWordIndex, currentSentenceIndex)

  val landmarksState = handLandmarkViewModel.landMarks().collectAsState()
  val detectedGesture = handLandmarkViewModel.getSolution()
  val exoCompletedText = stringResource(R.string.exercise_completed_text)
  if (!landmarksState.value.isNullOrEmpty()) {
    handleGestureMatching(
        detectedGesture = detectedGesture,
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        currentSentenceIndex = currentSentenceIndex,
        sentencesList = sentencesList,
        onProgressUpdate = { newLetterIndex, newWordIndex, newSentenceIndex ->
          currentLetterIndex = newLetterIndex
          currentWordIndex = newWordIndex
          currentSentenceIndex = newSentenceIndex
        },
        onAllSentencesComplete = {
          when (exerciseLevel.id) {
            ExerciseLevelName.EASY -> statsViewModel.updateEasyExerciseStats()
            ExerciseLevelName.MEDIUM -> statsViewModel.updateMediumExerciseStats()
            ExerciseLevelName.HARD -> statsViewModel.updateHardExerciseStats()
          }
          Toast.makeText(context, exoCompletedText, Toast.LENGTH_SHORT).show()
          // Reload or reset sentencesList, or handle end of exercise as needed
          currentSentenceIndex = 0
          currentWordIndex = 0
          currentLetterIndex = 0
        },
        trackTime = trackTime)
  }

  AnnexScreenScaffold(navigationActions = navigationActions, testTag = exerciseLevel.screenTag) {

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
            Icon(
                painter = painterResource(id = imageResId),
                contentDescription = "Sign image",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(120.dp))
          }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Sentence layer display
    SentenceLayer(
        sentencesList,
        currentLetterIndex,
        currentWordIndex,
        currentSentenceIndex,
    )

    // Camera placeholder/composable
    CameraBox(handLandmarkViewModel)
  }
}

/**
 * Manages progression through letters, words, and sentences in a text-based exercise.
 *
 * This function is called when a successful action occurs, such as a correctly detected gesture. It
 * updates the indices for the current letter, word, or sentence, and triggers the appropriate
 * callback based on the progression level.
 *
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence in the list of sentences.
 * @param sentences The list of sentences used in the exercise.
 * @param onProgressUpdate Callback to trigger progression to the next letter, word, or sentence.
 *   Takes new indices (newLetterIndex, newWordIndex, newSentenceIndex) as parameters to update the
 *   progression state.
 * @param onAllSentencesComplete Callback to be called when all sentences have been completed.
 */
fun onSuccess(
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int,
    sentences: List<String>,
    onProgressUpdate: (newLetterIndex: Int, newWordIndex: Int, newSentenceIndex: Int) -> Unit,
    onAllSentencesComplete: () -> Unit,
    trackTime: ExerciseTrackTime
) {
  // Retrieve the current sentence and split it into words
  val currentSentence = sentences[currentSentenceIndex]
  val words = currentSentence.split(" ")
  val currentWord = words[currentWordIndex]

  trackTime.updateTrackingAndCallUpdateTimePerLetter()

  when {
    // Move to the next letter within the current word
    currentLetterIndex < currentWord.length - 1 -> {
      onProgressUpdate(currentLetterIndex + 1, currentWordIndex, currentSentenceIndex)
    }

    // Move to the next word in the sentence
    currentWordIndex < words.size - 1 -> {
      onProgressUpdate(0, currentWordIndex + 1, currentSentenceIndex) // Reset letter index
    }

    // Move to the next sentence
    currentSentenceIndex < sentences.size - 1 -> {
      onProgressUpdate(0, 0, currentSentenceIndex + 1) // Reset both letter and word indices
    }

    // All sentences are completed
    else -> {
      onAllSentencesComplete()
    }
  }
}

/**
 * Composable function to display a layered view of sentences and words in an exercise.
 *
 * The back layer shows the current sentence with a semi-transparent style, highlighting the current
 * word. If the sentence contains only a single word, the next sentence is shown instead in
 * semi-transparent text. The front layer prominently displays the current word, with emphasis on
 * the current letter.
 *
 * @param sentences The list of sentences used in the exercise.
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence in the list of sentences.
 */
@Composable
fun SentenceLayer(
    sentences: List<String>,
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int
) {
  if (isIndexOutOfBounds(currentSentenceIndex, sentences.size)) return

  val wordsInSentence = getWordsInSentence(sentences, currentSentenceIndex) ?: return
  if (isIndexOutOfBounds(currentWordIndex, wordsInSentence.size)) return

  val currentWord = wordsInSentence[currentWordIndex]
  val nextSentence = getNextSentence(sentences, currentSentenceIndex)
  val isSingleWordSentence = wordsInSentence.size == 1

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
            text =
                buildBackgroundText(
                    wordsInSentence, nextSentence, currentWordIndex, isSingleWordSentence),
            modifier = Modifier.offset(y = (-40).dp).testTag("FullSentenceTag"),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
            style = TextStyle(fontSize = 24.sp))

        Text(
            text = buildForegroundText(currentWord, currentLetterIndex),
            modifier = Modifier.testTag("CurrentWordTag"),
            style = TextStyle(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSecondary)
      }
}

/**
 * Checks if the given index is out of bounds for a list of a specific size.
 *
 * @param index The index to check.
 * @param size The size of the list or array.
 * @return True if the index is out of bounds, false otherwise.
 */
fun isIndexOutOfBounds(index: Int, size: Int) = index >= size

/**
 * Retrieves and splits the specified sentence into words.
 *
 * @param sentences The list of sentences.
 * @param index The index of the sentence to retrieve.
 * @return A list of words from the sentence, or null if the index is out of bounds.
 */
fun getWordsInSentence(sentences: List<String>, index: Int): List<String>? {
  return sentences.getOrNull(index)?.split(" ")
}

/**
 * Retrieves the next sentence in the list, if available.
 *
 * @param sentences The list of sentences.
 * @param currentSentenceIndex The index of the current sentence.
 * @return The next sentence as a String, or an empty String if it is out of bounds.
 */
fun getNextSentence(sentences: List<String>, currentSentenceIndex: Int): String {
  return sentences.getOrNull(currentSentenceIndex + 1) ?: ""
}

/**
 * Retrieves the current letter based on the indices of the current sentence, word, and letter. This
 * function accesses the sentence, word, and letter specified by the provided indices and returns
 * the current letter in lowercase.
 *
 * @param sentencesList The list of sentences used in the exercise.
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence in the list of sentences.
 * @return The current letter as a lowercase character.
 */
fun getCurrentLetter(
    sentencesList: List<String>,
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int
): Char {
  val currentSentence = sentencesList[currentSentenceIndex]
  val wordsInCurrentSentence = currentSentence.split(" ")
  val currentWord = wordsInCurrentSentence[currentWordIndex]
  return currentWord[currentLetterIndex].lowercaseChar()
}

/**
 * Builds the background text for the back layer, which displays the entire sentence. Highlights the
 * current word in bold or displays the next sentence if it's a single-word sentence.
 *
 * @param wordsInSentence The list of words in the current sentence.
 * @param nextSentence The next sentence to display if needed.
 * @param currentWordIndex The index of the current word within the sentence.
 * @param isSingleWordSentence True if the sentence contains only one word.
 * @return An AnnotatedString for displaying the sentence.
 */
@Composable
fun buildBackgroundText(
    wordsInSentence: List<String>,
    nextSentence: String,
    currentWordIndex: Int,
    isSingleWordSentence: Boolean
): AnnotatedString {
  return buildAnnotatedString {
    if (isSingleWordSentence && nextSentence.isNotEmpty()) {
      append(nextSentence.lowercase())
    } else {
      wordsInSentence.forEachIndexed { index, word ->
        if (index == currentWordIndex) {
          withStyle(
              style =
                  SpanStyle(
                      color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)) {
                append(word)
              }
        } else {
          append(word)
        }
        if (index < wordsInSentence.size - 1) {
          append(" ")
        }
      }
    }
  }
}

/**
 * Builds the foreground text for the front layer, which displays only the current word. Highlights
 * the current letter within the word.
 *
 * @param currentWord The current word being displayed in the front layer.
 * @param currentLetterIndex The index of the current letter within the word.
 * @return An AnnotatedString for displaying the word with letter emphasis.
 */
@Composable
fun buildForegroundText(currentWord: String, currentLetterIndex: Int): AnnotatedString {
  return buildAnnotatedString {
    append(currentWord.substring(0, currentLetterIndex)) // Before the current letter
    withStyle(
        style =
            SpanStyle(
                letterSpacing = 10.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 50.sp)) {
          append(currentWord[currentLetterIndex].uppercase()) // Current letter in uppercase
    }
    append(currentWord.substring(currentLetterIndex + 1)) // After the current letter
  }
}

/**
 * Function to handle gesture matching logic in the exercise.
 *
 * This function compares the detected gesture with the current letter. If they match, it calls the
 * `onSuccess` function to handle progression through letters, words, and sentences. If they do not
 * match, a log message is printed indicating the mismatch.
 *
 * @param detectedGesture The gesture detected by the hand landmark model as a string.
 * @param currentLetterIndex The index of the current letter within the current word.
 * @param currentWordIndex The index of the current word within the current sentence.
 * @param currentSentenceIndex The index of the current sentence in the list of sentences.
 * @param sentencesList The list of sentences used in the exercise.
 * @param onProgressUpdate Callback to handle the update of indices for progression. Takes
 *   parameters (newLetterIndex, newWordIndex, newSentenceIndex) to update the current position
 *   within the exercise.
 * @param onAllSentencesComplete Callback to handle completion of all sentences in the exercise.
 */
fun handleGestureMatching(
    detectedGesture: String,
    currentLetterIndex: Int,
    currentWordIndex: Int,
    currentSentenceIndex: Int,
    sentencesList: List<String>,
    onProgressUpdate: (newLetterIndex: Int, newWordIndex: Int, newSentenceIndex: Int) -> Unit,
    onAllSentencesComplete: () -> Unit,
    trackTime: ExerciseTrackTime
) {
  val currentLetter =
      getCurrentLetter(sentencesList, currentLetterIndex, currentWordIndex, currentSentenceIndex)
  if (detectedGesture == currentLetter.uppercase()) {
    onSuccess(
        currentLetterIndex = currentLetterIndex,
        currentWordIndex = currentWordIndex,
        currentSentenceIndex = currentSentenceIndex,
        sentences = sentencesList,
        onProgressUpdate = onProgressUpdate,
        onAllSentencesComplete = onAllSentencesComplete,
        trackTime = trackTime)
  } else {
    Log.d(
        "ExerciseScreenEasy",
        "Detected gesture ($detectedGesture) does not match the current letter ($currentLetter)")
  }
}
