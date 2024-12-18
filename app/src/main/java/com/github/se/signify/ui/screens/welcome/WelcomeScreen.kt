package com.github.se.signify.ui.screens.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.signify.R
import com.github.se.signify.model.authentication.UserSession
import com.github.se.signify.model.navigation.NavigationActions
import com.github.se.signify.model.navigation.Screen
import com.github.se.signify.model.navigation.TopLevelDestinations
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    navigationActions: NavigationActions,
    userSession: UserSession,
) {

  // Check if the user is authenticated
  val isAuthenticated = userSession.isLoggedIn()

  // List of image resource ids for hand sign animation
  val images =
      listOf(
          R.drawable.letter_s,
          R.drawable.letter_i,
          R.drawable.letter_g,
          R.drawable.letter_n,
          R.drawable.letter_i,
          R.drawable.letter_f,
          R.drawable.letter_y)

  // Welcome text to display
  val welcomeText = stringResource(R.string.welcome_text)

  // Dynamically calculate the indices of "Signify" in the text
  val highlightIndices =
      remember(welcomeText) {
        val targetWord = "Signify"
        val startIndex = welcomeText.indexOf(targetWord)
        startIndex until (startIndex + targetWord.length)
      }

  // State to keep track of the current image index
  var currentImage by remember { mutableIntStateOf(0) }

  // LaunchedEffect to change the image every 1 second
  LaunchedEffect(Unit) {
    while (currentImage < images.size - 1) {
      delay(600) // delay between images
      currentImage += 1
    }
    delay(2000) // Pause after the last image

    if (isAuthenticated) {
      navigationActions.navigateTo(TopLevelDestinations.HOME)
    } else {
      navigationActions.navigateTo(Screen.AUTH)
    }
  }

  Column(
      modifier =
          Modifier.fillMaxSize()
              .testTag("WelcomeScreen")
              .background(color = MaterialTheme.colorScheme.primary),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Icon(
            painter = painterResource(id = images[currentImage]),
            contentDescription = "Hand Sign Images",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.width(100.dp).height(100.dp))
        // Highlight the corresponding letter in the welcome text
        if (currentImage < highlightIndices.count()) {
          HighlightedText(
              text = welcomeText, highlightIndex = highlightIndices.elementAt(currentImage))
        }
      }
}

@Composable
fun HighlightedText(text: String, highlightIndex: Int) {
  Text(
      text =
          buildAnnotatedString {
            // Iterate over each character in the string
            for (i in text.indices) {
              // If it's the character to highlight, underline it
              if (i == highlightIndex) {
                withStyle(
                    style =
                        SpanStyle(
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colorScheme.onPrimary)) {
                      append(text[i])
                    }
              } else {
                // Otherwise, keep the default style
                append(text[i])
              }
            }
          },
      modifier = Modifier.width(257.dp).height(150.dp),
      style =
          TextStyle(
              fontSize = 36.sp,
              lineHeight = 40.sp,
              fontWeight = FontWeight(500),
              color = MaterialTheme.colorScheme.onPrimary,
              textAlign = TextAlign.Center,
              letterSpacing = 0.25.sp,
          ))
}
