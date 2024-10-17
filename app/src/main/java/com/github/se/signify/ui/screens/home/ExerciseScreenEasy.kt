package com.github.se.signify.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.ui.navigation.NavigationActions

@Composable
fun ExerciseScreenEasy(navigationActions: NavigationActions) {

  val context = LocalContext.current

  // Placeholders, eventually have a viewmodel to pass arguments for the list of words.
  val word1 = "FGYF"
  val word2 = "FGYG"
  val word3 = "FGYY"

  val words = listOf(word1, word2, word3).map { it.lowercase() }

  // MutableState to keep track of the current letter
  var currentLetterIndex by rememberSaveable { mutableIntStateOf(0) }
  var currentWordIndex by rememberSaveable { mutableIntStateOf(0) }

  // Get the current letter based on the index
  val currentLetter = words[currentWordIndex][currentLetterIndex]

  Box(
      modifier = Modifier.fillMaxSize().background(Color.White),
      contentAlignment = Alignment.Center) {
        IconButton(
            onClick = { navigationActions.goBack() },
            modifier = Modifier.padding(16.dp).align(Alignment.TopStart).testTag("Back")) {
              Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = Color.Black)
            }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
              val imageName = "letter_${currentLetter}"
              val imageResId =
                  context.resources.getIdentifier(imageName, "drawable", context.packageName)

              // Display the image if the resource exists
              if (imageResId != 0) {
                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(150.dp)
                            .background(Color(0xFF05A9FB), shape = RoundedCornerShape(16.dp))
                            .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
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
              .background(Color(0xFF05A9FB), shape = RoundedCornerShape(16.dp))
              .border(2.dp, Color.Black, shape = RoundedCornerShape(16.dp)),
      contentAlignment = Alignment.Center) {
        // Next word (semi-transparent, slightly offset upwards)
        if (nextWord.isNotEmpty()) {
          Text(
              text = nextWord,
              modifier = Modifier.offset(y = (-40).dp).testTag("SecondWordTag"),
              color = Color.Gray.copy(alpha = 0.6f),
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
                          color = Color.Red, // Highlight color
                          fontSize = 50.sp // Larger size for the current letter
                          )) {
                    append(
                        currentWord[currentLetterIndex].uppercase()) // Current letter in uppercase
              }

              append(currentWord.substring(currentLetterIndex + 1)) // After the current letter
            },
            modifier = Modifier.testTag("FirstWordTag"),
            style = TextStyle(fontSize = 30.sp),
            color = Color.White)
      }
}

@Composable
fun SuccessButton(
    currentLetterIndex: Int,
    currentWordIndex: Int,
    words: List<String>,
    onNextLetter: (Int) -> Unit,
    onNextWord: (Int) -> Unit,
    onAllWordsComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize().padding(30.dp), contentAlignment = Alignment.BottomCenter) {
    Button(
        onClick = {
          onSuccess(
              currentLetterIndex = currentLetterIndex,
              currentWordIndex = currentWordIndex,
              words = words,
              onNextLetter = onNextLetter,
              onNextWord = onNextWord,
              onAllWordsComplete = onAllWordsComplete)
        },
        modifier = Modifier.width(150.dp).height(50.dp).testTag("Success")) {
          Text(text = "Success")
        }
  }
}

@Composable
fun CameraPlaceholder(modifier: Modifier = Modifier) {
  Box(
      modifier =
          modifier
              .fillMaxWidth()
              .padding(16.dp)
              .height(350.dp)
              .background(Color.Black, shape = RoundedCornerShape(16.dp))
              .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
      contentAlignment = Alignment.Center) {}
}
