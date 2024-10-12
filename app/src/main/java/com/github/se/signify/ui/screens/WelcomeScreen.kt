package com.github.se.signify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.github.se.signify.ui.navigation.NavigationActions
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navigationActions: NavigationActions) {

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
  val welcomeText = "Welcome to Signify"

  // Indexes of the letters to highlight
  val highlightIndices = listOf(11, 12, 13, 14, 15, 16, 17)

  // State to keep track of the current image index
  var currentImage by remember { mutableIntStateOf(0) }

  // LaunchedEffect to change the image every 1 second
  LaunchedEffect(Unit) {
    while (currentImage < images.size - 1) {
      delay(600) // delay between images
      currentImage += 1
    }
    delay(2000) // Pause after the last image
    navigationActions.navigateTo("Auth")
  }
  Column(
      modifier = Modifier.fillMaxSize().background(color = Color(0xFF05A9FB)),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = images[currentImage]),
            contentDescription = "Hand Sign Images",
            modifier = Modifier.width(100.dp).height(100.dp))
        // Highlight the corresponding letter in the welcome text
        HighlightedText(text = welcomeText, highlightIndex = highlightIndices[currentImage])
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
                        SpanStyle(textDecoration = TextDecoration.Underline, color = Color.White)) {
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
              color = Color(0xFFFFFFFF),
              textAlign = TextAlign.Center,
              letterSpacing = 0.25.sp,
          ))
}
